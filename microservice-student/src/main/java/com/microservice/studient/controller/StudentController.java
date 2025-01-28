package com.microservice.studient.controller;

import com.microservice.studient.http.request.StudentRequest;
import com.microservice.studient.http.response.StudentDetailsDTO;
import com.microservice.studient.service.StudentService;
import com.microservice.studient.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/create")
    public ResponseEntity<StudentDTO> addStudent(@RequestBody StudentRequest studentRequest) {
        // Usamos el metodo del servicio que mapea y guarda el Student
        StudentDTO savedStudentDTO = studentService.saveStudent(studentRequest);
        return new ResponseEntity<>(savedStudentDTO, HttpStatus.CREATED);
    }
    @GetMapping("/all")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        // Ahora retornamos una lista de StudentDTO
        List<StudentDTO> studentDTOs = studentService.findAll();
        return new ResponseEntity<>(studentDTOs, HttpStatus.OK);
    }
    @GetMapping("/search/{id}")
    public ResponseEntity<StudentDTO> searchStudent(@PathVariable Long id) {
        StudentDTO studentDTO = studentService.findById(id);
        if (studentDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Devuelve un 404 si no se encuentra el estudiante
        }
        return new ResponseEntity<>(studentDTO, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @RequestBody StudentRequest studentDTO) {
        // Usamos el método del servicio para actualizar el estudiante y devolverlo como DTO
        StudentDTO updatedStudentDTO = studentService.updateStudent(id, studentDTO);
        return ResponseEntity.ok(updatedStudentDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // 204 No Content cuando se elimina correctamente
    }




    //Comunicacon entre el microserve de course

    //Search student by courseId
    @GetMapping("/search-by-course/{idCourse}")
    public ResponseEntity<List<StudentDTO>> findByIdCourse(@PathVariable Long idCourse) {
        // Retornamos una lista de StudentDTOs filtrados por curso
        List<StudentDTO> studentDTOs = studentService.findByIdCourse(idCourse);
        return new ResponseEntity<>(studentDTOs, HttpStatus.OK);
    }
    @PutMapping("/update-course-id/{id}")
    public ResponseEntity<Void> updateStudentsCourseId(@PathVariable Long id) {
        studentService.updateStudentsCourseId(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<StudentDetailsDTO> getStudentDetails(@PathVariable Long id) {
        StudentDetailsDTO studentDetailsDTO = studentService.findStudentDetailsById(id);

        return new ResponseEntity<>(studentDetailsDTO, HttpStatus.OK);
    }
}
