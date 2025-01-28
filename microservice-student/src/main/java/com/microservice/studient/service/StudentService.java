package com.microservice.studient.service;

import com.microservice.studient.dto.StudentDTO;
import com.microservice.studient.dto.TeacherDTO;
import com.microservice.studient.http.request.StudentRequest;
import com.microservice.studient.http.response.StudentDetailsDTO;

import java.util.List;

public interface StudentService {

    StudentDTO saveStudent(StudentRequest studentRequest);
    StudentDTO findById(Long id);
    List<StudentDTO> findAll();
    void deleteById(Long id);
    StudentDTO updateStudent(Long id, StudentRequest studentDTO);


    List<StudentDTO> findByIdCourse(Long idCourse);
    void updateStudentsCourseId(Long idCourse);
    StudentDetailsDTO findStudentDetailsById(Long id);

}
