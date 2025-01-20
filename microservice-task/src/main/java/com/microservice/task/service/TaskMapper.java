package com.microservice.task.service;

import com.microservice.task.DTO.TaskDTO;
import com.microservice.task.entity.Task;
import com.microservice.task.http.request.teacher.TeacherTaskRequest;

public interface TaskMapper {

    Task convertRequestToTask(TeacherTaskRequest taskRequest);
    TaskDTO convertTaskToDTO(Task task);
    Task prepareUpdatedTask(Long id, TeacherTaskRequest taskRequest);
}
