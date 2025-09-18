package com.example.library_management.repository;


import com.example.library_management.model.Student;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Transactional
    @Modifying //A must when writing modifying queries
    @Query("UPDATE Student s SET s.status = 'INACTIVE' WHERE s.id=:id")
    void deactivate(Long id);
}
