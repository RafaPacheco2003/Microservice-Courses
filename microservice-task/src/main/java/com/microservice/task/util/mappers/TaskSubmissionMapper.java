package com.microservice.task.util.mappers;

import com.microservice.task.DTO.TaskSubmissionDTO;
import com.microservice.task.entity.TaskSubmission;

import java.util.List;

public interface TaskSubmissionMapper {
    TaskSubmissionDTO convertToDTO(TaskSubmission taskSubmission);

    List<TaskSubmissionDTO> convertToDTOList(List<TaskSubmission> taskSubmissions);
}
