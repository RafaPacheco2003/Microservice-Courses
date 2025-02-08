package com.microservice.task.util.mappers;

import com.microservice.task.DTO.TaskDTO;
import com.microservice.task.entity.Task;
import com.microservice.task.http.request.teacher.TeacherTaskRequest;

public interface TaskMapper {

    Task convertTeacherTaskRequestToTask(TeacherTaskRequest taskRequest);
    TaskDTO convertTaskToTaskDTO(Task task);
    Task convertUpdateTeacherTaskRequestToTaskDTO(Long id, TeacherTaskRequest taskRequest);

}
