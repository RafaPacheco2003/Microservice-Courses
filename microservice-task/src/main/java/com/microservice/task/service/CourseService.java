package com.microservice.task.service;

import com.microservice.task.DTO.CourseDTO;

public interface CourseService {
    CourseDTO getCourseById(Long courseId);
}
