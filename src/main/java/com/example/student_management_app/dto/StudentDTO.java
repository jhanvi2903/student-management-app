package com.example.student_management_app.dto;

import jakarta.validation.constraints.*;

public class StudentDTO {

    @NotBlank(message = "name is mandatory")
    private String name;

    @Min(value = 0, message = "marks cannot be less than 0")
    @Max(value = 100, message = "marks cannot be greater than 100")
    @PositiveOrZero(message = "marks cannot be negative")
    private String marks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }
}
