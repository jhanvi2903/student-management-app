package com.example.student_management_app.repository;

import com.example.student_management_app.model.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity,Integer> {
    List<StudentEntity> findByName(String name);
}
