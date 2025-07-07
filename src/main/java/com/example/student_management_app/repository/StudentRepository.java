package com.example.student_management_app.repository;

import com.example.student_management_app.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {
    List<Student> findByName(String name);
}
