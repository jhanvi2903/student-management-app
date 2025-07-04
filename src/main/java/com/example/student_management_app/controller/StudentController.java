package com.example.student_management_app.controller;

import com.example.student_management_app.dto.StudentDTO;
import com.example.student_management_app.model.Student;
import com.example.student_management_app.service.StudentService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private ModelMapper modelMapper;


    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody @Valid StudentDTO studentDTO) {
        Student student = modelMapper.map(studentDTO,Student.class);
        Student savedStudent = studentService.createStudent(student);
        URI location = URI.create("api/v1/students" + student.getId());

        return ResponseEntity.created(location).body(modelMapper.map(savedStudent, StudentDTO.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable int id) {
        Student student = studentService.getStudentById(id);

        return ResponseEntity.ok(modelMapper.map(student, StudentDTO.class));
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<Student> student = studentService.getAllStudents();
        List<StudentDTO> studentDTOS = student.stream().map(student1 -> modelMapper.map(student1, StudentDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok(studentDTOS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable int id, @RequestBody @Valid StudentDTO studentDTO) {
        Student updatedStudent = studentService.updateStudent(id, modelMapper.map(studentDTO, Student.class));
        return ResponseEntity.ok(modelMapper.map(updatedStudent, StudentDTO.class));
    }

}
