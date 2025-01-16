package com.microservice.task.client;

import com.microservice.task.DTO.StudentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@FeignClient(name = "msvc-student", url = "localhost:8080/api/student")
public interface StudentClient {

    @GetMapping("/search/{id}")
    StudentDTO searchStudent(@PathVariable("id") Long idStuden);

    @GetMapping("/search-by-course/{idCourse}")
    List<StudentDTO> findByIdCourse(@PathVariable Long idCourse);
}
