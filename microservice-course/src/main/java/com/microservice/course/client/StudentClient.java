package com.microservice.course.client;

import com.microservice.course.dto.StudentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
//microservice-studient"
@FeignClient(name = "msvc-student", url = "localhost:8080/api/student")
public interface StudentClient {


    @GetMapping("/search-by-course/{idCourse}")
    List<StudentDTO > findAllStudentsByCourse(@PathVariable Long idCourse);

    @PutMapping("/update-course-id/{id}")
    void updateStudentsCourseId(@PathVariable("id") Long id);

}
