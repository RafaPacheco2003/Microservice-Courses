package com.microservice.task.service.impl;

import com.microservice.task.DTO.CourseDTO;
import com.microservice.task.client.CourseClient;
import com.microservice.task.exception.TaskNotFoundException;
import com.microservice.task.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseClient courseClient;

    @Override
    public CourseDTO getCourseById(Long courseId) {
        CourseDTO course = courseClient.searchCourse(courseId);
        if (course == null) {
            throw new TaskNotFoundException("Course not found");
        }
        return course;
    }
}
