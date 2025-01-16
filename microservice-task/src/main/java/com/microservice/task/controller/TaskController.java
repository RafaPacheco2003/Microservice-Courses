package com.microservice.task.controller;

import com.microservice.task.DTO.TaskDTO;
import com.microservice.task.http.request.teacher.TeacherTaskGradeRequest;
import com.microservice.task.http.request.teacher.TeacherTaskRequest;
import com.microservice.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TeacherTaskRequest taskRequest) {
        TaskDTO savedTask= taskService.addTask(taskRequest);
        return ResponseEntity.ok().body(savedTask);
    }

    @GetMapping("/search-task-by-studentId/{studentId}")
    public ResponseEntity<List<TaskDTO>> getTasksByStudent(@PathVariable("studentId") Long studentId) {
        System.out.println("Entrando a student");
        List<TaskDTO> tasks = taskService.getTasksByStudentId(studentId);
        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/search-task-by-courseId/{courseId}")
    public ResponseEntity<List<TaskDTO>> getTasksByCourse(@PathVariable("courseId") Long courseId) {
        System.out.println("Entrando a student");
        List<TaskDTO> tasks = taskService.getTasksByStudentId(courseId);
        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/search/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(taskService.getTaskById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTaskById(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    // Endpoint para obtener todas las tareas
    @GetMapping("/all")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> taskDTOList = taskService.getAllTasks();
        return ResponseEntity.ok(taskDTOList);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TeacherTaskRequest taskRequest) {
        TaskDTO updatedTask = taskService.updateTask(id, taskRequest);
        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/grade/{id}")
    public ResponseEntity<TaskDTO> gradeTask(@PathVariable Long id, @RequestBody TeacherTaskGradeRequest gradeRequest) {
        TaskDTO updatedTask = taskService.gradeTask(id, gradeRequest);
        return ResponseEntity.ok(updatedTask);
    }

}
