package com.microservice.task.service.impl;

import com.microservice.task.DTO.TeacherDTO;
import com.microservice.task.client.TeacherClient;
import com.microservice.task.exception.TaskNotFoundException;
import com.microservice.task.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherClient teacherClient; // Asegúrate de inyectar el cliente de TeacherClient


    @Override
    public TeacherDTO getTeacherById(Long teacherId) {
        TeacherDTO teacher = teacherClient.searchTeacher(teacherId);
        if (teacher == null) {
            throw new TaskNotFoundException("Teacher not found");
        }
        return teacher;
    }

}
