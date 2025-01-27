package com.microservice.task.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class TaskSubmissionDTO {
    private Long id;
    private Long studentId;
    private boolean submitted;
    private boolean isLate;
    private String studentComment;
    private Double grade;
    private String teacherComment;
    private Date submissionDate;
    private String taskTitle;  // Título de la tarea

    // Puedes agregar más detalles según necesites
}
