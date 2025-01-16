package com.microservice.teacher.client;

import com.microservice.teacher.dto.CourseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "msvc-course", url = "localhost:8080/api/course")
public interface CourseClient {

    @GetMapping("/search-by-teacher/{teacherId}")
    List<CourseDTO> findCourseByTeacherId(@PathVariable("teacherId") Long teacherId);

    @PutMapping("/update-course-by-teacherId/{teacherId}")
    void updateCourseByTeacherId(@PathVariable("teacherId")  Long teacherId);
}
