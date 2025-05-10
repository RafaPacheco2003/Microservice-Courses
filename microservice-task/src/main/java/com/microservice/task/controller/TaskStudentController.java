package com.microservice.task.controller;

import com.microservice.task.DTO.TaskDTO;
import com.microservice.task.DTO.TaskSubmissionDTO;
import com.microservice.task.DTO.TaskWithSubmissionsDTO;
import com.microservice.task.http.request.student.TaskSubmissionRequest;
import com.microservice.task.http.request.teacher.GradeTaskRequest;
import com.microservice.task.service.TaskService;
import com.microservice.task.service.TaskSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Este controller puede buscar tareas por su id y por el course que tiene y podra subir las tareas que tenga asignadas
 */

@RestController
@RequestMapping("/api/task/student")
public class TaskStudentController {

    @Autowired
    private TaskSubmissionService taskSubmissionService;
    @Autowired
    private TaskService taskService;



    //Obtener task por tyaskId
    @GetMapping("/search/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable("taskId") Long taskId) {
        return ResponseEntity.ok().body(taskService.getTaskById(taskId));
    }
    //Obtener tarea por el course
    @GetMapping("/search-task-by-courseId/{courseId}")
    public ResponseEntity<List<TaskDTO> > getTaskByCourseId(@PathVariable Long courseId) {
        List<TaskDTO> tasksDTO = taskService.getTasksByCourse(courseId);
        return ResponseEntity.ok(tasksDTO);
    }

    //Obtener la tarea por el id de student por token
    @GetMapping("/search-task-by-studentId/{studentId}")
    public ResponseEntity<List<TaskDTO>> getTasksByStudentId(@PathVariable Long studentId) {
        List<TaskDTO> tasksDTO = taskService.getTasksByStudentId(studentId);
        return ResponseEntity.ok(tasksDTO);
    }


    @PostMapping(value = "/{taskId}/submit/{studentId}", consumes = "multipart/form-data")
    public ResponseEntity<?> submitTask(
            @PathVariable Long taskId,
            @PathVariable Long studentId,
            @RequestPart("submission") TaskSubmissionRequest submissionRequest,
            @RequestPart("pdfFile") MultipartFile pdfFile) {

        try {
            submissionRequest.setPdfFile(pdfFile); // Asignamos el archivo al DTO
            TaskSubmissionDTO taskSubmissionDTO = taskSubmissionService.submitTaskOnce(studentId, taskId, submissionRequest);
            return ResponseEntity.ok(taskSubmissionDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }


    @GetMapping("/{studentId}/details/submmit/{taskId}")
    public ResponseEntity<TaskWithSubmissionsDTO> getTaskWithSubmissionsForStudent(
            @PathVariable Long taskId,
            @PathVariable Long studentId) {

        // Llamar al servicio para obtener las entregas filtradas por el studentId
        TaskWithSubmissionsDTO taskWithSubmissions = taskSubmissionService.getTaskWithSubmissionsForStudent(studentId, taskId);

        // Devolver la respuesta con el objeto TaskWithSubmissionsDTO
        return ResponseEntity.ok(taskWithSubmissions);
    }












}
