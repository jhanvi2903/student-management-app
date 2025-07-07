package com.example.student_management_app.controller;

import com.example.student_management_app.dto.StudentDTO;
import com.example.student_management_app.model.Student;
import com.example.student_management_app.service.StudentService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.modelmapper.Converters.Collection.map;

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

    @GetMapping("id/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable int id) {
        Student student = studentService.getStudentById(id);

        return ResponseEntity.ok(modelMapper.map(student, StudentDTO.class));
    }

    @GetMapping
    public ResponseEntity<Page<StudentDTO>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "marks") String sortBy) {
        int size = 10;
        Sort sort = Sort.by(sortBy).descending();
        Page<Student> studentPage = studentService.getAllStudents(PageRequest.of(page, size, sort));
        Page<StudentDTO> studentDTOS = studentPage.map(student1 -> modelMapper.map(student1, StudentDTO.class));
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

    @GetMapping("name/{name}")
    public ResponseEntity<List<StudentDTO>> getStudentByName(@PathVariable String name) {
        List<Student> students = studentService.getStudentByName(name);

        List<StudentDTO> studentDTOs = students.stream()
                .map(student -> modelMapper.map(student, StudentDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(studentDTOs);
    }


}
