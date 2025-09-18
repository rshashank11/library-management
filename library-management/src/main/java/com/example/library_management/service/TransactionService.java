package com.example.library_management.service;

import com.example.library_management.model.*;
import com.example.library_management.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    @Value("${student.book.max-allowance}")
    private Integer maxAllowance;

    private final StudentService studentService;

    private final BookService bookService;

    private final TransactionRepository transactionRepository;

    public Transaction initiateTranscation(Long studentId, Long bookId, TransactionType transactionType) throws Exception {
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
        Transaction transaction = this.transactionRepository.save(mapToTransaction(student, book));
        try {
            book.setStudent(student);
            this.bookService.updateBookAvailability(book);
            transaction.setStatus(Status.COMPLETED);
            transaction = this.transactionRepository.save(transaction);
        } catch (Exception e) {
            transaction.setStatus(Status.FAILED);
            transaction = this.transactionRepository.save(transaction);
            book.setStudent(null);
            this.bookService.updateBookAvailability(book);
        }
        return transaction;
    }

    private Transaction mapToTransaction(Student student, Book book) {
        return Transaction.builder().externalTransactionId(UUID.randomUUID())
                .student(student)
                .book(book)
                .type(TransactionType.ISSUE)
                .status(Status.PENDING)
                .build();
    }

    private Transaction initiateReturn(Long studentId, Long bookId) {
        return null;
    }
}
