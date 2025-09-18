package com.example.library_management.DTO;

import com.example.library_management.model.Student;
import jakarta.persistence.Column;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class CreateStudentRequest {
    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = true)
    private String email;

    @Column(unique = true, nullable = false)
    private String mobile;

    public Student mapToStudent() {
        return Student.builder()
                .name(this.name)
                .mobile(this.mobile)
                .email(this.email)
                .build();
    }
}
