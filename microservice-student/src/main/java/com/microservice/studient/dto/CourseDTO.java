package com.microservice.studient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDTO {
    private Long id;        // ID del curso
    private String name;    // Nombre del curso
    private String teacher; // Nombre del profesor
}
