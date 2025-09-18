package com.example.library_management.service;

import com.example.library_management.DTO.CreateBookRequest;
import com.example.library_management.model.Author;
import com.example.library_management.model.Book;
import com.example.library_management.repository.AuthorRepository;
import com.example.library_management.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final AuthorService authorService;

    public List<Book> getBooksByStudentId(Long studentId) {
        return this.bookRepository.findByStudentId(studentId);
    }

    public Long createBook(CreateBookRequest createBookRequest) {
        Book incomingBook = createBookRequest.mapToBookAndAuthor();
        Author author = authorService.getOrCreateAuthor(incomingBook.getAuthor());
        incomingBook.setAuthor(author);
        this.bookRepository.save(incomingBook);
        return incomingBook.getId();
    }

    public Book getBookById(Long bookId) {
        return this.bookRepository.findById(bookId).orElseThrow(null);
    }

    public Book updateBookAvailability(Book book) {
        return this.bookRepository.save(book);
    }
}
