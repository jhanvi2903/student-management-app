package com.example.student_management_app.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class DataValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleDataValidationException(MethodArgumentNotValidException exc) {
        Map<String, String> errorsMap = new HashMap<>();

        // Handles field level error
        exc.getBindingResult().getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String message = error.getDefaultMessage();

            errorsMap.put(fieldName, message);
        });

        // Handles class level error, such as custom password matches annotation
        exc.getBindingResult().getGlobalErrors().forEach(error -> {
            // Use object name or custom key for class-level errors
            errorsMap.put(error.getObjectName(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleStudentNotFoundException(StudentNotFoundException exc) {
        Map<String, String> errorResponse = new HashMap<>();

        errorResponse.put("error", exc.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", HttpStatus.NOT_FOUND.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
