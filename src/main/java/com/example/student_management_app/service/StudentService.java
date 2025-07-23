package com.example.student_management_app.service;

import com.example.student_management_app.dto.StudentRequestDTO;
import com.example.student_management_app.exception.StudentNotFoundException;
import com.example.student_management_app.model.StudentEntity;
import com.example.student_management_app.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    private ModelMapper modelMapper;

    public StudentEntity createStudent(StudentRequestDTO student) {
        StudentEntity entity = modelMapper.map(student, StudentEntity.class);
        return studentRepository.save(entity);
    }

    public StudentEntity getStudentById(int id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student " + id + " not found"));
    }

    public Page<StudentEntity> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    public void deleteStudent(int id) {
        studentRepository.deleteById(id);
    }

    public StudentEntity updateStudent(int id, StudentEntity student) {
        StudentEntity existingStudent = getStudentById(id);
        existingStudent.setName(student.getName());
        existingStudent.setMarks(student.getMarks());

        studentRepository.save(existingStudent);

        return existingStudent;
    }

    public List<StudentEntity> getStudentByName(String name) {
        List<StudentEntity> students = studentRepository.findByName(name);

        if(students.isEmpty()) {
            throw new StudentNotFoundException("Student " + name + " not found");
        }

        return students;
    }

}
