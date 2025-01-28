package com.microservice.studient.client;

import com.microservice.studient.dto.TeacherDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-teacher", url = "http://localhost:8080/api/teacher")
public interface TeacherClient {

    @GetMapping("/search/{id}")
    TeacherDTO getTeacher(@PathVariable("id") Long id);
}
