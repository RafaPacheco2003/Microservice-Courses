package com.microservice.studient.client;

import com.microservice.studient.dto.CourseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-course", url = "http://localhost:8080/api/course")
public interface CourseClient {
    @GetMapping("/{id}")
    CourseDTO getCourseById(@PathVariable("id") Long courseId);
}
