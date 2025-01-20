package com.microservice.task.service.impl;

import com.microservice.task.DTO.StudentDTO;
import com.microservice.task.client.StudentClient;
import com.microservice.task.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentClient studentClient;


    @Override
    public List<Long> getStudentIdsByCourse(Long courseId) {
        List<StudentDTO> studentDTOList = studentClient.findByIdCourse(courseId);
        return studentDTOList.stream()
                .map(StudentDTO::getId)  // Extraer el ID de cada estudiante
                .collect(Collectors.toList());  // Recolectar los resultados en una lista

    }

    @Override
    public StudentDTO getStudentById(Long studentId) {
        StudentDTO studentDTO = studentClient.searchStudent(studentId);
        if (studentDTO == null) {
            throw new RuntimeException("Student With Id: " + studentId + " not found");
        }
        return studentDTO;
    }
}
