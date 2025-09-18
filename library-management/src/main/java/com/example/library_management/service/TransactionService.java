package com.example.library_management.service;

import com.example.library_management.model.*;
import com.example.library_management.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    @Value("${student.book.max-allowance}")
    private Integer maxAllowance;

    @Value("${book.return-duration}")
    private Integer returnDuration;

    @Value("${book.fine-per-day}")
    private Double finePerDay;

    private final StudentService studentService;

    private final BookService bookService;

    private final TransactionRepository transactionRepository;

    private Transaction mapToTransaction(Student student, Book book, TransactionType transactionType, Status status, Double fine) {
        return Transaction.builder().externalTransactionId(UUID.randomUUID())
                .student(student)
                .book(book)
                .type(transactionType)
                .status(status)
                .fine(fine)
                .build();
    }

    public Transaction initiateTransacation(Long studentId, Long bookId, TransactionType transactionType) throws Exception {
        switch(transactionType){
            case ISSUE -> {
                return initiateIssuance(studentId, bookId);
            }
            case RETURN -> {
                return initiateReturn(studentId, bookId);
            }
            default -> {
                throw new Exception("Invalid Transaction Type");
            }
        }
    }

    private Transaction initiateIssuance(Long studentId, Long bookId) throws Exception {
        Student student = this.studentService.getStudentDetails(studentId).getStudent();
        Book book = this.bookService.getBookById(bookId);
        if(student == null) {
            throw new Exception("Student not found.");
        }

        if(book == null || book.getStudent() != null) {
            throw new Exception("Book is not available for issuance.");
        }

        List<Book> issuedBooks = student.getBookList();
        if(issuedBooks != null && issuedBooks.size() >= maxAllowance) {
            throw new Exception("Student has issued maximum number of books.");
        }
        Transaction transaction = this.transactionRepository.save(mapToTransaction(student, book, TransactionType.ISSUE, Status.PENDING, null));
        try {
            book.setStudent(student);
            this.bookService.updateBookAvailability(book);
            transaction.setStatus(Status.COMPLETED);
            transaction = this.transactionRepository.save(transaction);
        } catch (Exception e) {
            transaction.setStatus(Status.FAILED);
            transaction = this.transactionRepository.save(transaction);
            //If transaction failed, set it to null
            book.setStudent(null);
            this.bookService.updateBookAvailability(book);
        }
        return transaction;
    }



    private Double calculateFine(Book book, Student student) {
        Transaction transaction = this.transactionRepository.findTopByBookAndStudentAndTypeAndStatusOrderByIdDesc(book, student, TransactionType.ISSUE, Status.COMPLETED);

        LocalDate today = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate issuedDate = transaction.getUpdatedOn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Long daysInBetween = ChronoUnit.DAYS.between(issuedDate, today);

            if(daysInBetween > returnDuration) {
                return ((daysInBetween - returnDuration) * finePerDay);
        }
        return null;
    }

    private Transaction initiateReturn(Long studentId, Long bookId) throws Exception {
        Student student = this.studentService.getStudentDetails(studentId).getStudent();
        Book book = this.bookService.getBookById(bookId);
        if(student == null) {
            throw new Exception("Student not found.");
        }

        if(book == null || book.getStudent() == null || book.getStudent().getId() != studentId) {
            throw new Exception("Book is not available for return.");
        }

        Double fine = this.calculateFine(book, student);
        System.out.println(fine);

        Transaction transaction = this.transactionRepository.save(mapToTransaction(student, book, TransactionType.RETURN, Status.PENDING, fine));
        try {
            book.setStudent(null);
            this.bookService.updateBookAvailability(book);
            transaction.setStatus(Status.COMPLETED);
            transaction = this.transactionRepository.save(transaction);
        } catch (Exception e) {
            transaction.setStatus(Status.FAILED);
            transaction = this.transactionRepository.save(transaction);
            //If transaction failed, set it back to student
            book.setStudent(student);
            this.bookService.updateBookAvailability(book);
        }
        return transaction;
    }


    public Page<Transaction> getStudentTransactions(Long studentId, Pageable pageable) {
        return this.transactionRepository.findAllByStudentId(studentId, pageable);
    }
}
