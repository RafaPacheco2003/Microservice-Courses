package com.microservice.task.DTO;

import java.util.List;

public class TaskWithStudentsDTO {
    private Long id;
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private Long teacherId;
    private Long courseId;
    private List<Long> studentIds;  // Lista de IDs de estudiantes
}
