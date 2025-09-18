package com.example.library_management.service;

import com.example.library_management.model.Author;
import com.example.library_management.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {

    public final AuthorRepository authorRepository;
    public Author getOrCreateAuthor(Author author) {
        Author savedAuthor = this.authorRepository.findByEmailIgnoreCase(author.getEmail());

        if(savedAuthor == null) {
            this.authorRepository.save(author);
            return author;
        }

        return savedAuthor;
    }
}
