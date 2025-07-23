package com.example.student_management_app.controller;

import com.example.student_management_app.dto.StudentRequestDTO;
import com.example.student_management_app.dto.StudentResponseDTO;
import com.example.student_management_app.exception.StudentNotFoundException;
import com.example.student_management_app.model.StudentEntity;
import com.example.student_management_app.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.core.type.TypeReference;


import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = StudentController.class)
public class StudentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    StudentService studentService;

    @MockitoBean
    ModelMapper modelMapper;

    ObjectMapper objectMapper = new ObjectMapper();
    StudentRequestDTO studentRequestDTO;

    @BeforeEach
    void setup() {
        studentRequestDTO = new StudentRequestDTO();
        studentRequestDTO.setName("Jhanvi");
        studentRequestDTO.setEmail("jhanvi@gmail.com");
        studentRequestDTO.setMarks(56);
        studentRequestDTO.setPassword("jhanvi_12345");
        studentRequestDTO.setRepeatPassword("jhanvi_12345");
    }


    @Test
    @DisplayName("Student can be created")
    void testCreateStudent_whenValidStudentDetailsProvided_thenReturnsCreatedStudentDetails() throws Exception {
        // Arrange
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(4);
        studentEntity.setName(studentRequestDTO.getName());
        studentEntity.setEmail(studentRequestDTO.getEmail());
        studentEntity.setMarks(studentRequestDTO.getMarks());

        StudentResponseDTO studentResponseDTO = new StudentResponseDTO();
        studentResponseDTO.setName(studentEntity.getName());
        studentResponseDTO.setEmail(studentEntity.getEmail());
        studentResponseDTO.setMarks(studentEntity.getMarks());
        studentResponseDTO.setId(4);

        when(modelMapper.map(any(StudentRequestDTO.class), eq(StudentEntity.class))).thenReturn(studentEntity);
        when(studentService.createStudent(any(StudentRequestDTO.class))).thenReturn(studentEntity);
        when(modelMapper.map(any(StudentEntity.class), eq(StudentResponseDTO.class))).thenReturn(studentResponseDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentRequestDTO));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();
        StudentResponseDTO createdStudent = objectMapper.readValue(responseBodyAsString, StudentResponseDTO.class);

        System.out.println(createdStudent.getName());
        // Assert
        Assertions.assertEquals(studentRequestDTO.getName(), createdStudent.getName(), "The student name should be correct");
        Assertions.assertEquals(studentRequestDTO.getEmail(), createdStudent.getEmail(), "The student email should be correct");
        Assertions.assertEquals(studentRequestDTO.getMarks(), createdStudent.getMarks(), "The student marks should be correct");
        Assertions.assertNotNull(studentEntity.getId());
    }

    @Test
    @DisplayName("If name is not entered then returns 400 status code")
    void testCreateUser_whenNameIsNotProvided_returns400StatusCode() throws Exception {
        // Arrange
        studentRequestDTO.setName(null); // Set name to null to trigger @NotBlank validation

        System.out.println(objectMapper.writeValueAsString(studentRequestDTO));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentRequestDTO));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),
                mvcResult.getResponse().getStatus(),
                "Incorrect HTTP Status Code returned");
    }

    @Test
    @DisplayName("Get student by id when student does not exist")
    void testGetStudentById_whenStudentDoesNotExist_shouldReturn404NotFound() throws Exception {
        // Arrange
        when(studentService.getStudentById(1)).thenThrow(new StudentNotFoundException("Student 1 not found"));

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/students/id/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus(), "Student not found");
        String responseBody = mvcResult.getResponse().getContentAsString();
        Map<String, String> errorMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, String>>() {});
        Assertions.assertEquals("Student 1 not found", errorMap.get("error"));
    }

    @Test
    @DisplayName("Get student by name when student does not exist")
    void testGetStudentByName_whenStudentDoesNotExist_shouldReturn404NotFound() throws Exception {
        // Arrange
        when(studentService.getStudentByName("John")).thenThrow(new StudentNotFoundException("Student John not found"));

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/students/name/John")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // Assert
        String responseBody = mvcResult.getResponse().getContentAsString();
        Map<String, String> errorMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, String>>() {});

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus(), "Student not found");
        Assertions.assertEquals("Student John not found", errorMap.get("error"));

    }
}
