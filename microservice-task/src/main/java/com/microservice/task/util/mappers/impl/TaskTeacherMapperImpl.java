package com.microservice.task.util.mappers.impl;

import com.microservice.task.DTO.TaskDTO;
import com.microservice.task.entity.Task;
import com.microservice.task.http.request.teacher.TeacherTaskRequest;
import com.microservice.task.util.mappers.TaskMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TaskTeacherMapperImpl implements TaskMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public Task convertTeacherTaskRequestToTask(TeacherTaskRequest taskRequest) {
        Task task = new Task();

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setType(taskRequest.getType());
        task.setCreationDate(taskRequest.getCreationDate());
        task.setStartDate(taskRequest.getStartDate());
        task.setEndDate(taskRequest.getEndDate());
        task.setTeacherId(taskRequest.getTeacherId());
        task.setCourseId(taskRequest.getCourseId());
        return task;
    }

    @Override
    public TaskDTO convertTaskToTaskDTO(Task task) {
        return modelMapper.map(task, TaskDTO.class);
    }

    @Override
    public Task convertUpdateTeacherTaskRequestToTaskDTO(Long id, TeacherTaskRequest taskRequest) {
        Task updatedTask = convertRequestToTask(taskRequest);
        updatedTask.setId(id);
        updatedTask.setUpdatedDate(new Date());
        return updatedTask;
    }
}
