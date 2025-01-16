package com.microservice.studient.service;

import com.microservice.studient.dto.StudentDTO;

import java.util.List;

public interface StudentService {

    StudentDTO saveStudent(StudentDTO StudentDTO);
    StudentDTO findById(Long id);
    List<StudentDTO> findAll();
    void deleteById(Long id);
    StudentDTO updateStudent(Long id, StudentDTO studentDTO);


    List<StudentDTO> findByIdCourse(Long idCourse);
    void updateStudentsCourseId(Long idCourse);

}
