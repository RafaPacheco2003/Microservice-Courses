package com.microservice.task.controller;

import com.microservice.task.DTO.TaskDTO;
import com.microservice.task.DTO.TaskSubmissionDTO;
import com.microservice.task.http.request.teacher.GradeTaskRequest;
import com.microservice.task.http.request.teacher.TeacherTaskGradeRequest;
import com.microservice.task.http.request.teacher.TeacherTaskRequest;
import com.microservice.task.service.TaskTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskTeacherService taskTeacherService;

    @PostMapping("/create")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TeacherTaskRequest taskRequest) {
        TaskDTO savedTask= taskTeacherService.addTask(taskRequest);
        return ResponseEntity.ok().body(savedTask);
    }

    @GetMapping("/search-task-by-studentId/{studentId}")
    public ResponseEntity<List<TaskDTO>> getTasksByStudent(@PathVariable("studentId") Long studentId) {
        System.out.println("Entrando a student");
        List<TaskDTO> tasks = taskTeacherService.getTasksByStudentId(studentId);
        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/search-task-by-courseId/{courseId}")
    public ResponseEntity<List<TaskDTO>> getTasksByCourse(@PathVariable("courseId") Long courseId) {
        System.out.println("Entrando a student");
        List<TaskDTO> tasks = taskTeacherService.getTasksByStudentId(courseId);
        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/search/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(taskTeacherService.getTaskById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTaskById(@PathVariable("id") Long id) {
        taskTeacherService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    // Endpoint para obtener todas las tareas
    @GetMapping("/all")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> taskDTOList = taskTeacherService.getAllTasks();
        return ResponseEntity.ok(taskDTOList);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TeacherTaskRequest taskRequest) {
        TaskDTO updatedTask = taskTeacherService.updateTask(id, taskRequest);
        return ResponseEntity.ok(updatedTask);
    }

    @PostMapping("/grade/{submissionId}")
    public ResponseEntity<TaskSubmissionDTO> gradeTaskSubmission(
            @PathVariable Long submissionId,
            @RequestBody GradeTaskRequest gradeTaskRequest) {

        // Llamar al servicio para calificar la entrega de tarea
        TaskSubmissionDTO gradedSubmission = taskTeacherService.gradeTaskSubmission(submissionId, gradeTaskRequest);

        // Retornar la entrega actualizada
        return ResponseEntity.ok(gradedSubmission);
    }

}
