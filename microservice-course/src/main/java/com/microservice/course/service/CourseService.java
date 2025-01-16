package com.microservice.course.service;

import com.microservice.course.dto.CourseDTO;
import com.microservice.course.entity.Course;
import com.microservice.course.http.response.StudentByCourseResponse;

import java.util.List;

public interface CourseService {
    List<CourseDTO> getAllCourses();
    CourseDTO getCourseById(Long courseId);
    CourseDTO addCourse(CourseDTO courseDTO);
    void deleteCourseById(Long courseId);

    StudentByCourseResponse findStudentByIdCoourse(Long idCourse);

    List<CourseDTO> findByTeacherId(Long teacherId);
    void updateCourseByTeacherId(Long teacherId);

}
