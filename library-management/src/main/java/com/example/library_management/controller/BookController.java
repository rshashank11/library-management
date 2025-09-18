package com.example.library_management.controller;


import com.example.library_management.DTO.CreateBookRequest;
import com.example.library_management.model.Book;
import com.example.library_management.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public Long createBook(@Valid @RequestBody CreateBookRequest createBookRequest) {
        return bookService.createBook(createBookRequest);
    }

    @GetMapping("/{bookId}")
    public Book getBook(@PathVariable("bookId") Long bookId) {
        return bookService.getBookById(bookId);
    }
}
