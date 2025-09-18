package com.example.library_management.DTO;

import com.example.library_management.model.Author;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CreateAuthorRequest {
    private String name;

    @Column(unique = true)
    private String email;

    public Author mapToAuthor() {
        return Author.builder().
        name(this.name).
                email(this.email)
        .build();
    }
}
