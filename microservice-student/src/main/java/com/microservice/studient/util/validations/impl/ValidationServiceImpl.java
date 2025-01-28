package com.microservice.studient.util.validations.impl;

import com.microservice.studient.dto.CourseDTO;
import com.microservice.studient.entity.Student;
import com.microservice.studient.exception.StudentNotFoundException;
import com.microservice.studient.persistence.StudentRepository;
import com.microservice.studient.util.validations.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student validateStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

}
