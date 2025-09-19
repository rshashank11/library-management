package com.example.library_management.service;

import com.example.library_management.DTO.StudentDTO;
import com.example.library_management.model.*;
import com.example.library_management.repository.StudentRepository;
import com.example.library_management.repository.TransactionRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    // Will make an actual object.
    // Used for the thing you are testing.
    @InjectMocks // Similar to @Component - the object of transactionService will be created and attached with the test class.
    TransactionService transactionService;

    // Used for the dependencies
    @Mock // Creates fake object /without any real logic
    TransactionRepository transactionRepository;

    @Mock
    StudentService studentService;

    @Mock
    BookService bookService;

    @Mock
    StudentRepository studentRepository;

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        // Manually set the @Value fields using reflection
        // This is a standard approach for Mockito tests with @Value annotations
        setPrivateField(transactionService, "returnDuration", 14);
        setPrivateField(transactionService, "finePerDay", 5.0);
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    public void testCalculateFine() {
        Book book = Book.builder().id(1L).name("History 101").build();
        Student student = Student.builder().id(11L).
                name("Shashank").
                email("shashank@gmail.com").
                mobile("13131313").build();

        long twentyDaysAgo = System.currentTimeMillis() - (20L * 24L * 60L * 60L * 1000L);
        Transaction transaction = Transaction.builder().id(1L).
                updatedOn(new Date(twentyDaysAgo)).
                student(student).
                type(TransactionType.ISSUE).
                status(Status.COMPLETED).
                book(book).build();

        Mockito.when(transactionRepository.findTopByBookAndStudentAndTypeAndStatusOrderByIdDesc(
                        Mockito.eq(book),
                        Mockito.eq(student),
                        Mockito.eq(TransactionType.ISSUE),
                        Mockito.eq(Status.COMPLETED))).
                thenReturn(transaction);

        double fine = transactionService.calculateFine(book, student);
        Assert.assertEquals(30.0, fine, 0.01);
        System.out.println(fine);
    }

    @Test
    public void testInitiateReturn() throws Exception {
        Student student = Student.builder().id(1l).build();

        Book book = Book.builder().id(5l).student(student).build();

        StudentDTO studentDTO = StudentDTO.builder().
                student(student).
                books(new ArrayList<>()).
                build();

        Transaction transaction = Transaction.builder().id(1L).
                updatedOn(new Date()).externalTransactionId(UUID.randomUUID()).
                student(student).
                type(TransactionType.RETURN).
                status(Status.COMPLETED).
                book(book).build();


        Mockito.when(studentService.getStudentDetails(1l)).thenReturn(studentDTO);
        Mockito.when(bookService.getBookById(5l)).thenReturn(book);
//        Mockito.when(studentRepository.findById(student.getId())).thenReturn(studentDTO);
        Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(transaction);
        Mockito.when(transactionRepository.findTopByBookAndStudentAndTypeAndStatusOrderByIdDesc(
                        Mockito.eq(book),
                        Mockito.eq(student),
                        Mockito.eq(TransactionType.ISSUE),
                        Mockito.eq(Status.COMPLETED))).
                thenReturn(transaction);
        Mockito.when(bookService.updateBookAvailability(Mockito.any())).thenReturn(book);
        Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(transaction);
        Transaction finalTransaction = transactionService.initiateReturn(1l, 5l);
        System.out.println(finalTransaction.getType());
    }
}

