package com.example.library_management.controller;


import com.example.library_management.model.Transaction;
import com.example.library_management.model.TransactionType;
import com.example.library_management.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/initiate")
    public Transaction initiateTransaction(@RequestParam("studentId") Long studentId,
                                           @RequestParam("bookId") Long bookId) throws Exception {
        return this.transactionService.initiateTranscation(studentId, bookId, TransactionType.ISSUE);
    }
}
