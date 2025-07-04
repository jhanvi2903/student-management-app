package com.example.student_management_app.mapper;

import com.example.student_management_app.dto.CourseDTO;
import com.example.student_management_app.model.Course;

public class CourseMapper {

    public static CourseDTO toDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO(course.getCourseName(), course.getDescription());
        return courseDTO;
    }

    public static Course toEntity(CourseDTO courseDTO) {
        Course course = new Course();
        course.setCourseName(courseDTO.getCourseName());
        course.setDescription(courseDTO.getDescription());
        return course;
    }
}
