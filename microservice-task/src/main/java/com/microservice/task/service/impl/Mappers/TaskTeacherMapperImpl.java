package com.microservice.task.service.impl.Mappers;

import com.microservice.task.DTO.TaskDTO;
import com.microservice.task.entity.Task;
import com.microservice.task.http.request.teacher.TeacherTaskRequest;
import com.microservice.task.service.TaskMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class TaskTeacherMapperImpl implements TaskMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public Task convertRequestToTask(TeacherTaskRequest taskRequest) {
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
    public TaskDTO convertTaskToDTO(Task task) {
        return modelMapper.map(task, TaskDTO.class);
    }

    @Override
    public Task prepareUpdatedTask(Long id, TeacherTaskRequest taskRequest) {
        Task updatedTask = convertRequestToTask(taskRequest);
        updatedTask.setId(id);
        return updatedTask;
    }
}
