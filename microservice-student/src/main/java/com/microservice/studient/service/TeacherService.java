package com.microservice.studient.service;

import com.microservice.studient.dto.TeacherDTO;

public interface TeacherService {
    TeacherDTO getTeacherById(Long id);
}
