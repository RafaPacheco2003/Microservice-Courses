package com.microservice.task.service;

import com.microservice.task.DTO.StudentDTO;

import java.util.List;

public interface StudentService {

    List<Long> getStudentIdsByCourse(Long courseId);
    StudentDTO getStudentById(Long courseId);
}
