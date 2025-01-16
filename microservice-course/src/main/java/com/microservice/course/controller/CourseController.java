package com.microservice.course.controller;


import com.microservice.course.dto.CourseDTO;
import com.microservice.course.entity.Course;
import com.microservice.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/course")
@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;


    @GetMapping("/all")
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        // Usamos el servicio para obtener la lista de cursos como DTOs
        return new ResponseEntity<>(courseService.getAllCourses(), HttpStatus.OK);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable("id") Long id) {
        // Usamos el servicio para obtener el curso por su id como DTO
        return new ResponseEntity<>(courseService.getCourseById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<CourseDTO> addCourse(@RequestBody CourseDTO courseDTO) {
        // Guardamos el curso y lo devolvemos como DTO
        CourseDTO savedCourse = courseService.addCourse(courseDTO);
        return new ResponseEntity<>(savedCourse, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCourseById(@PathVariable("id") Long id) {
        // Usamos el servicio para eliminar el curso por su id
        courseService.deleteCourseById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content cuando se elimina correctamente
    }

    //Client
    @GetMapping("/search-student/{idCourse}")
    public ResponseEntity<?> getStudentsByIdCourse(@PathVariable Long idCourse) {
        return  ResponseEntity.ok(courseService.findStudentByIdCoourse(idCourse));
    }


    //COMUNICATION WITH A TEACHER
    @GetMapping("/search-by-teacher/{teacherId}")
    public ResponseEntity<List<CourseDTO>> findCoursesByIdTeacher(@PathVariable Long teacherId) {
        List<CourseDTO> courseDTOS = courseService.findByTeacherId(teacherId);
        return ResponseEntity.ok(courseDTOS);
    }

    @PutMapping("/update-course-by-teacherId/{teacherId}")
    public ResponseEntity<Void> updateCourseByTeacherId(@PathVariable Long teacherId) {
        courseService.updateCourseByTeacherId(teacherId);
        return ResponseEntity.ok().build(); // Devuelve una respuesta sin cuerpo
    }




}
