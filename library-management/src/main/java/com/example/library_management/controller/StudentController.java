package com.example.library_management.controller;

import com.example.library_management.DTO.CreateStudentRequest;
import com.example.library_management.DTO.StudentDTO;
import com.example.library_management.DTO.UpdateStudentRequest;
import com.example.library_management.model.Student;
import com.example.library_management.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @PostMapping
    public Long createStudent(@RequestBody CreateStudentRequest createStudentRequest) {
        Long id = studentService.createStudent(createStudentRequest);
        return id;
    }

    @GetMapping("/{studentId}")
    public StudentDTO getStudentDetails(@PathVariable("studentId") Long studentId) {
        StudentDTO studentDTO = studentService.getStudentDetails(studentId);
        return studentDTO;
    }

    @PatchMapping("/{studentId}")
    public StudentDTO updateStudent(@Valid @RequestBody UpdateStudentRequest updateStudentRequest, @PathVariable(name = "studentId") Long studentId) {
        StudentDTO studentDTO = studentService.updateStudentDetails(updateStudentRequest, studentId);
        return studentDTO;
    }

    @DeleteMapping("/{studentId}")
    public StudentDTO deactivateStudent(@PathVariable("studentId") Long studentId) {
        StudentDTO studentDTO = studentService.deactivateStudent(studentId);
        return studentDTO;
    }

}
