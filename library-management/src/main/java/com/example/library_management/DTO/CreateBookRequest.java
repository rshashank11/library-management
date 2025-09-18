package com.example.library_management.DTO;

import com.example.library_management.model.Author;
import com.example.library_management.model.Book;
import com.example.library_management.model.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateBookRequest {

    private Genre genre;

    @NotBlank
    private String bookName;

    private String authorName;

    @NotBlank
    private String authorEmail;

    public Book mapToBookAndAuthor() {
        return Book.builder().
                name(this.bookName).
                genre(this.genre).
                author(Author.builder().
                        name(authorName).
                        email(authorEmail).
                        build())
        .build();
    }
}
