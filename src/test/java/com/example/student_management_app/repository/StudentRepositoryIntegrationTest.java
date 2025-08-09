package com.example.student_management_app.repository;

import com.example.student_management_app.model.Course;
import com.example.student_management_app.model.StudentEntity;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class StudentRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    StudentEntity studentEntity;
    Course course;

    @BeforeEach
    void setup() {
        course = new Course();
        course.setCourseName("CS");
        course.setDescription("Computer Science");


        studentEntity = new StudentEntity();

        studentEntity.setName("Jhanvi");
        studentEntity.setEmail("jhanvi@gmail.com");
        studentEntity.setCourse(course);
        studentEntity.setMarks(97);
        studentEntity.setPassword("1234");
        studentEntity.setRepeatPassword("1234");
    }
    
    @Test
    void testStudentEntity_whenValidStudentDetailsProvided_shouldReturnStoredStudentDetails() {

        // Act
        Course course1 = testEntityManager.persistAndFlush(course);
        StudentEntity storedStudentEntity = testEntityManager.persistAndFlush(studentEntity);

        // Assert
        Assertions.assertEquals(studentEntity.getName(), storedStudentEntity.getName());
        Assertions.assertEquals(studentEntity.getEmail(), storedStudentEntity.getEmail());
        Assertions.assertEquals(studentEntity.getCourse(), storedStudentEntity.getCourse());
        Assertions.assertEquals(studentEntity.getMarks(), storedStudentEntity.getMarks());
        Assertions.assertEquals(studentEntity.getPassword(), storedStudentEntity.getPassword());
        Assertions.assertEquals(studentEntity.getRepeatPassword(), storedStudentEntity.getRepeatPassword());
    }

    @Test
    void testStudentEntity_whenFirstNameIsTooLong_shouldThrowException() {
         // Arrange
        studentEntity.setName("jhgdfstjhyrvjhnosiuyftvbatratshabxkiagyvhjbcjayfgvhgtubavfrdxfhioaxbhv");

        // Act & Assert
        Assertions.assertThrows(PersistenceException.class, ()-> {
            testEntityManager.persistAndFlush(studentEntity);
        });
    }

    @Test
    void testStudentEntity_whenExistingStudenEmailProvided_shouldThrowException() {
        // Arrange
        StudentEntity newstudentEntity = new StudentEntity();
        newstudentEntity.setName("Neha");
        newstudentEntity.setEmail("neha@gmail.com");
        newstudentEntity.setMarks(98);
        newstudentEntity.setPassword("1234");
        newstudentEntity.setRepeatPassword("1234");
        testEntityManager.persistAndFlush(newstudentEntity);

        studentEntity.setEmail("neha@gmail.com");

        // Act & Assert
        Assertions.assertThrows(PersistenceException.class, () -> {
            testEntityManager.persistAndFlush(studentEntity);
        });
    }
}
