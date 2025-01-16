package com.microservice.teacher.controller;

import com.microservice.teacher.dto.TeacherDTO;
import com.microservice.teacher.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping("/create")
    public ResponseEntity<TeacherDTO> createTeacher(@RequestBody TeacherDTO teacherDTO) {
        TeacherDTO savedTeacher= teacherService.addTeacher(teacherDTO);
        return ResponseEntity.ok().body(savedTeacher);
    }
    @GetMapping("/search/{id}")
    public ResponseEntity<TeacherDTO> getTeacher(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(teacherService.getTeacherById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>deleteTeacher(@PathVariable("id") Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search-course/{teacherId}")
    public ResponseEntity<?> getCourseByTeacherId(@PathVariable("teacherId") Long teacherId) {
        return ResponseEntity.ok(teacherService.findCourseByTeacherId(teacherId));
    }


}
