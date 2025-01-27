package com.microservice.task.http.request.student;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class TaskSubmissionRequest {
    private String studentComment;
    private Date submissionDate;  // Fecha de entrega de la tarea
    private MultipartFile pdfFile;  // Archivo PDF entregado por el estudiante
}
