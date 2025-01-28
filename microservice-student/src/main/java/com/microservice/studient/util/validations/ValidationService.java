package com.microservice.studient.util.validations;

import com.microservice.studient.dto.CourseDTO;
import com.microservice.studient.entity.Student;

public interface ValidationService {
    Student validateStudent(Long id);
}
