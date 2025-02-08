package com.microservice.task.util.mappers;

import com.microservice.task.DTO.TaskSubmissionDTO;
import com.microservice.task.entity.Task;
import com.microservice.task.entity.TaskSubmission;
import com.microservice.task.http.request.student.TaskSubmissionRequest;

import java.util.List;

public interface TaskSubmissionMapper {
    TaskSubmissionDTO convertTaskSubmissionToTaskSubmissionDTO(TaskSubmission taskSubmission);

    List<TaskSubmissionDTO> convertListTaskSubmissionToListTaskSubmissionDTO(List<TaskSubmission> taskSubmissions);

    TaskSubmission convertTaskSubmissionRequestToEntity(Long studentId, TaskSubmissionRequest submissionRequest, Task task, String storedFilePath);
}
