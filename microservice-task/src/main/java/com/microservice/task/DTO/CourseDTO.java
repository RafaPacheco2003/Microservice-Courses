package com.microservice.task.DTO;


import lombok.Data;

@Data
public class CourseDTO {
    private Long id;        // ID del curso
    private String name;    // Nombre del curso
    private String teacher; // Nombre del profesor
}
