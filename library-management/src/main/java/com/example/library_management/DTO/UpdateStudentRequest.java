package com.example.library_management.DTO;

import com.example.library_management.model.Student;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class UpdateStudentRequest {
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
