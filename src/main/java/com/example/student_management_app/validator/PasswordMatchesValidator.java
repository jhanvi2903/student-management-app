package com.example.student_management_app.validator;

import com.example.student_management_app.customannotation.PasswordMatches;
import com.example.student_management_app.dto.StudentRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, StudentRequestDTO> {
//    @Override
//    public void initialize(PasswordMatches constraintAnnotation) {
//        ConstraintValidator.super.initialize(constraintAnnotation);
//    }

    @Override
    public boolean isValid(StudentRequestDTO studentDTO, ConstraintValidatorContext constraintValidatorContext) {
        if(studentDTO.getPassword().equals(studentDTO.getRepeatPassword())) {
            return true;
        } else {
            return false;
        }
    }
}
