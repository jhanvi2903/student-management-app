package com.example.student_management_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CourseDTO {

    public CourseDTO(String courseName, String description) {
        this.courseName = courseName;
        this.description = description;
    }

    @NotBlank(message = "course name is mandatory")
    private String courseName;

    @NotBlank(message = "description is required")
    @Size(min = 0, max = 7)
    private String description;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
