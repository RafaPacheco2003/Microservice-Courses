package com.microservice.task.client;

import com.microservice.task.DTO.CourseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-course", url = "http://localhost:8080/api/course")
public interface CourseClient {

    @GetMapping("/search/{id}")
    CourseDTO searchCourse(@PathVariable("id") Long courseId);
}
