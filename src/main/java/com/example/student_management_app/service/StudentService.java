package com.example.student_management_app.service;

import com.example.student_management_app.exception.StudentNotFoundException;
import com.example.student_management_app.model.Student;
import com.example.student_management_app.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudentById(int id) {
        return studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("user not found"));
    }

    public Page<Student> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    public void deleteStudent(int id) {
        studentRepository.deleteById(id);
    }

    public Student updateStudent(int id, Student student) {
        Student existingStudent = getStudentById(id);
        existingStudent.setName(student.getName());
        existingStudent.setMarks(student.getMarks());

        studentRepository.save(existingStudent);

        return existingStudent;
    }

    public List<Student> getStudentByName(String name) {
        List<Student> students = studentRepository.findByName(name);

        if(students.isEmpty()) {
            throw new StudentNotFoundException("Student " + name + " not found");
        }

        return students;
    }

}
