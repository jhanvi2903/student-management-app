package com.example.student_management_app.repository;

import com.example.student_management_app.model.Course;
import com.example.student_management_app.model.StudentEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    StudentRepository studentRepository;

    @Test
    void testStudentEntity_whenValidStudentNameProvided_shouldReturnSearchedNameStudentDetail() {
        Course course = new Course();
        course.setCourseName("CS");
        course.setDescription("Computer Science");
        testEntityManager.persistAndFlush(course);

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setName("Jhanvi");
        studentEntity.setEmail("jhanvi@gmail.com");
        studentEntity.setCourse(course);
        studentEntity.setMarks(97);
        studentEntity.setPassword("1234");
        studentEntity.setRepeatPassword("1234");
        testEntityManager.persistAndFlush(studentEntity);

        List<StudentEntity> returnedStudentEntity = studentRepository.findByName(studentEntity.getName());

        Assertions.assertEquals(studentEntity.getName(), returnedStudentEntity.get(0).getName());
    }
}
