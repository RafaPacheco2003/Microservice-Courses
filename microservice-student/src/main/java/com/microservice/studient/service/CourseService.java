package com.microservice.studient.service;

import com.microservice.studient.dto.CourseDTO;

public interface CourseService {
    CourseDTO getCourseById(Long courseId);
}
