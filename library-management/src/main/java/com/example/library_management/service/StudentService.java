package com.example.library_management.service;

import com.example.library_management.DTO.CreateStudentRequest;
import com.example.library_management.DTO.StudentDTO;
import com.example.library_management.DTO.UpdateStudentRequest;
import com.example.library_management.model.Book;
import com.example.library_management.model.Student;
import com.example.library_management.model.StudentStatus;
import com.example.library_management.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class StudentService {

    public Object deleteStudent;
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    BookService bookService;

    private final ObjectMapper objectMapper;

    public StudentService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public StudentDTO getStudentDetails(Long studentId) {
        Student student = this.studentRepository.findById(studentId).orElse(null);
        return StudentDTO.builder()
                .student(student)
                .build();
    }

    public Long createStudent(CreateStudentRequest createStudentRequest) {
        Student student = createStudentRequest.mapToStudent();
        student = this.studentRepository.save(student); //ID is being added to the same object when saving
        return student.getId();
    }

    @Transactional
    public StudentDTO updateStudentDetails(UpdateStudentRequest updateStudentRequest, Long id) {
        Student incomingStudent = updateStudentRequest.mapToStudent();
        StudentDTO studentDTO =  this.getStudentDetails(id);
        Student existingStudent = studentDTO.getStudent();
       existingStudent = this.deepMerge(incomingStudent, existingStudent);
        this.studentRepository.save(existingStudent);
        return this.getStudentDetails(id);
    }

    //Required to null check or else 500 error will be thrown as certain fields have certain constraints
    private Student deepMerge(Student incoming, Student existing) {
        JSONObject incomingStudent = objectMapper.convertValue(incoming, JSONObject.class);
        JSONObject existingStudent = objectMapper.convertValue(existing, JSONObject.class);

        Iterator it = incomingStudent.keySet().iterator();
        while(it.hasNext()) {
            String key = (String) it.next();
            if(incomingStudent.get(key) != null) {
                existingStudent.put(key, incomingStudent.get(key));
            }
        }

        return objectMapper.convertValue(existingStudent, Student.class);
    }

    public StudentDTO deactivateStudent(Long id) {
        this.studentRepository.deactivate(id);
        return getStudentDetails(id);
    }
}
