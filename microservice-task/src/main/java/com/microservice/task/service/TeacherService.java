package com.microservice.task.service;

import com.microservice.task.DTO.TeacherDTO;

public interface TeacherService {

    TeacherDTO getTeacherById(Long teacherId);
}
