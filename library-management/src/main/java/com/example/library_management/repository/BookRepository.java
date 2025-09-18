package com.example.library_management.repository;

import com.example.library_management.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByStudentId(Long studentId);
}
