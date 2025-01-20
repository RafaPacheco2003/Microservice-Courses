package com.microservice.task.service;

import com.microservice.task.DTO.TaskDTO;
import com.microservice.task.http.request.teacher.TeacherTaskGradeRequest;
import com.microservice.task.http.request.teacher.TeacherTaskRequest;

import java.util.List;

public interface TaskTeacherService {

    TaskDTO addTask(TeacherTaskRequest taskRequest);
    TaskDTO getTaskById(Long id);
    TaskDTO updateTask(Long id, TeacherTaskRequest taskRequest);
    TaskDTO gradeTask(Long id, TeacherTaskGradeRequest gradeRequest);
    void deleteTask(Long id);
    List<TaskDTO> getAllTasks();

    List<TaskDTO> getTasksByStudentId(Long studentId);
    List<TaskDTO> getTasksByCourse(Long courseId);

}
