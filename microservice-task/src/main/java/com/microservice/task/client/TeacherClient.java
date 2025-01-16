package com.microservice.task.client;

import com.microservice.task.DTO.TeacherDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-teacher", url = "http://localhost:8080/api/teacher") // URL estática correcta
public interface TeacherClient {
    @GetMapping("/search/{id}")
    TeacherDTO searchTeacher(@PathVariable("id") Long id);
}