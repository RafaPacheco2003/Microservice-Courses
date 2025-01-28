package com.microservice.studient.service.impl;

import com.microservice.studient.client.TeacherClient;
import com.microservice.studient.dto.TeacherDTO;
import com.microservice.studient.exception.StudentNotFoundException;
import com.microservice.studient.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherClient teacherClient;

    @Override
    public TeacherDTO getTeacherById(Long id) {
        TeacherDTO teacherDTO = teacherClient.getTeacher(id);
        if (teacherDTO == null) {
            throw new StudentNotFoundException("Teacher Not Found");
        }

        return teacherDTO;
    }
}

