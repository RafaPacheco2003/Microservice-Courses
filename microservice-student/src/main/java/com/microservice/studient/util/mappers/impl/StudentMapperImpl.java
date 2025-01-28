package com.microservice.studient.util.mappers.impl;

import com.microservice.studient.dto.StudentDTO;
import com.microservice.studient.entity.Student;
import com.microservice.studient.http.request.StudentRequest;
import com.microservice.studient.util.mappers.StudentMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentMapperImpl implements StudentMapper {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Student CreateRequestToEntity(StudentRequest studentRequest) {
        return modelMapper.map(studentRequest, Student.class);
    }

    @Override
    public void UpdateRequestToEntity(StudentRequest studentRequest, Student existingStudent) {
        modelMapper.map(studentRequest, existingStudent);
    }

    @Override
    public StudentDTO EntityToDTO(Student student) {
        return modelMapper.map(student, StudentDTO.class);
    }
}
