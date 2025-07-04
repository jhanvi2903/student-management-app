package com.example.student_management_app.service;

import com.example.student_management_app.model.Course;
import com.example.student_management_app.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(int id) {
        return courseRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Course not found"));
    }

    public Course updateCourse(int id, Course course) {
        Course existingCourse = getCourseById(id);

        existingCourse.setCourseName(course.getCourseName());
        existingCourse.setDescription(course.getDescription());

        courseRepository.save(existingCourse);

        return existingCourse;
    }
}
