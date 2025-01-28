package com.microservice.studient.util.mappers;

import com.microservice.studient.dto.StudentDTO;
import com.microservice.studient.entity.Student;
import com.microservice.studient.http.request.StudentRequest;

public interface StudentMapper {
    Student CreateRequestToEntity(StudentRequest studentRequest);
    void UpdateRequestToEntity(StudentRequest studentRequest, Student student);
    StudentDTO EntityToDTO(Student student);
}
