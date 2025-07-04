package com.example.student_management_app.controller;

import com.example.student_management_app.dto.CourseDTO;
import com.example.student_management_app.mapper.CourseMapper;
import com.example.student_management_app.model.Course;
import com.example.student_management_app.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController()
@RequestMapping("api/v1/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody @Valid CourseDTO coursedto) {
        Course course = CourseMapper.toEntity(coursedto);
        Course savedCourse = courseService.createCourse(course);
        URI location = URI.create("api/v1/courses" + course.getId());
        return ResponseEntity.created(location).body(CourseMapper.toDTO(savedCourse));
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<Course> course = courseService.getAllCourses();
        List<CourseDTO> courseDTO = course.stream().map(CourseMapper :: toDTO).toList();
        return ResponseEntity.ok(courseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable int id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(CourseMapper.toDTO(course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable int id, @RequestBody @Valid CourseDTO courseDTO) {
        Course course = CourseMapper.toEntity(courseDTO);
        Course updatedCourse = courseService.updateCourse(id, course);
        return ResponseEntity.ok(CourseMapper.toDTO(updatedCourse));
    }
}
