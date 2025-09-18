package com.example.library_management.controller;


import com.example.library_management.model.Student;
import com.example.library_management.model.Transaction;
import com.example.library_management.model.TransactionType;
import com.example.library_management.service.TransactionService;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/initiate")
    public Transaction initiateTransaction(@RequestParam("studentId") Long studentId,
                                           @RequestParam("bookId") Long bookId,
                                           @RequestParam("transactionType") TransactionType transactionType) throws Exception {
        return this.transactionService.initiateTransacation(studentId, bookId, transactionType);
    }

    @GetMapping("/student/{studentId}")
    private Page<Transaction> getStudentTransactions(@PathVariable("studentId") Long studentId) {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(0, 5, sort);
        return this.transactionService.getStudentTransactions(studentId, pageable);
    }

}
