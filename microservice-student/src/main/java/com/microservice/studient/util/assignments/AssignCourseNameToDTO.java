package com.microservice.studient.util.assignments;

import com.microservice.studient.client.CourseClient;
import com.microservice.studient.dto.CourseDTO;
import com.microservice.studient.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssignCourseNameToDTO {

    @Autowired
    private CourseClient courseClient;

    // Metodo auxiliar para asignar el nombre del curso al StudentDTO
    public void assignCourseNameToDTO(StudentDTO studentDTO) {
        Long courseId = studentDTO.getCourseId();
        CourseDTO course = null;
        try {
            course = courseClient.getCourseById(courseId);
        } catch (Exception e) {
            // Manejar error si no se puede obtener el curso
            System.out.println("Error obteniendo el curso para el estudiante con ID: " + studentDTO.getId());
        }

        // Asignar el nombre del curso al StudentDTO
        if (course != null) {
            studentDTO.setCourseName(course.getName());
        } else {
            studentDTO.setCourseName("Course not found");
        }
    }
}
