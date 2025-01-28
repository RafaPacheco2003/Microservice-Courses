package com.microservice.task.controller;

import com.microservice.task.DTO.TaskSubmissionDTO;
import com.microservice.task.DTO.TaskWithSubmissionsDTO;
import com.microservice.task.http.request.student.TaskSubmissionRequest;
import com.microservice.task.http.request.teacher.GradeTaskRequest;
import com.microservice.task.service.TaskStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/student/tasks")
public class TaskStudentController {

    @Autowired
    private TaskStudentService taskService;


    @PostMapping("/{taskId}/submit/{studentId}")
    public ResponseEntity<TaskSubmissionDTO> submitTask(
            @PathVariable Long taskId,
            @PathVariable Long studentId,
            @RequestParam String studentComment, // El comentario del estudiante
            @RequestParam String submissionDate, // La fecha de entrega
            @RequestParam MultipartFile pdfFile) { // El archivo PDF

        // Convertir la fecha de entrega desde String a Date
        Date submissionDateParsed = new Date(); // Parsear la fecha correctamente

        TaskSubmissionRequest submissionRequest = new TaskSubmissionRequest();
        submissionRequest.setStudentComment(studentComment);
        submissionRequest.setSubmissionDate(submissionDateParsed);
        submissionRequest.setPdfFile(pdfFile); // Obtener los bytes del archivo

        // Llamar al servicio para entregar la tarea
        try {
            TaskSubmissionDTO taskSubmissionDTO = taskService.submitTask(studentId, taskId, submissionRequest);
            return ResponseEntity.ok(taskSubmissionDTO);
        } catch (Exception e) { // Capturando cualquier excepción genérica
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskWithSubmissionsDTO> getTaskWithSubmissions(@PathVariable Long taskId) {
        TaskWithSubmissionsDTO taskWithSubmissions = taskService.getTaskWithSubmissions(taskId);
        return ResponseEntity.ok(taskWithSubmissions);
    }

}
