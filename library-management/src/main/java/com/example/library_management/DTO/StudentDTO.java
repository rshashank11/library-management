package com.example.library_management.DTO;

import com.example.library_management.model.Book;
import com.example.library_management.model.Student;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class StudentDTO {
    private Student student;


    private List<Book> books;
}
