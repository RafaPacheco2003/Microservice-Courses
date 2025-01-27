package com.microservice.studient.service.impl;

import com.microservice.studient.client.CourseClient;
import com.microservice.studient.dto.CourseDTO;
import com.microservice.studient.exception.StudentNotFoundException;
import com.microservice.studient.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseClient courseClient;
    @Override
    public CourseDTO getCourseById(Long courseId) {
        CourseDTO courseDTO = courseClient.getCourseById(courseId);
        if (courseDTO == null) {
            throw new StudentNotFoundException("Course not found");
        }
        return courseDTO;
    }
}
