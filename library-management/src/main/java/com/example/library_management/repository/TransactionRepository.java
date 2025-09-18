package com.example.library_management.repository;

import com.example.library_management.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, PagingAndSortingRepository<Transaction, Long> {
    Transaction findTopByBookAndStudentAndTypeAndStatusOrderByIdDesc(Book book, Student student, TransactionType transactionType, Status status);
    Page<Transaction> findAllByStudentId(Long studentId, Pageable pageable);
}
