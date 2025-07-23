package com.example.student_management_app.controller;

import com.example.student_management_app.dto.StudentRequestDTO;
import com.example.student_management_app.dto.StudentResponseDTO;
import com.example.student_management_app.model.StudentEntity;
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

@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private ModelMapper modelMapper;


    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@RequestBody @Valid StudentRequestDTO studentDTO) {
        StudentEntity savedStudent = studentService.createStudent(studentDTO);
        if (savedStudent != null) {
            URI location = URI.create("api/v1/students" + savedStudent.getId());
            return ResponseEntity.created(location).body(modelMapper.map(savedStudent, StudentResponseDTO.class));
        }

        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("id/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable int id) {
        StudentEntity student = studentService.getStudentById(id);

        return ResponseEntity.ok(modelMapper.map(student, StudentResponseDTO.class));
    }

    @GetMapping
    public ResponseEntity<Page<StudentResponseDTO>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "marks") String sortBy) {
        int size = 10;
        Sort sort = Sort.by(sortBy).descending();
        Page<StudentEntity> studentPage = studentService.getAllStudents(PageRequest.of(page, size, sort));
        Page<StudentResponseDTO> studentDTOS = studentPage.map(student1 -> modelMapper.map(student1, StudentResponseDTO.class));
        return ResponseEntity.ok(studentDTOS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(@PathVariable int id, @RequestBody @Valid StudentRequestDTO studentDTO) {
        StudentEntity updatedStudent = studentService.updateStudent(id, modelMapper.map(studentDTO, StudentEntity.class));
        return ResponseEntity.ok(modelMapper.map(updatedStudent, StudentResponseDTO.class));
    }

    @GetMapping("name/{name}")
    public ResponseEntity<List<StudentResponseDTO>> getStudentByName(@PathVariable String name) {
        List<StudentEntity> students = studentService.getStudentByName(name);

        List<StudentResponseDTO> studentDTOs = students.stream()
                .map(student -> modelMapper.map(student, StudentResponseDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(studentDTOs);
    }


}
