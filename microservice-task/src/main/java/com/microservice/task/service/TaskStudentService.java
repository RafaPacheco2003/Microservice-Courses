package com.microservice.task.service;

import com.microservice.task.DTO.TaskSubmissionDTO;
import com.microservice.task.DTO.TaskWithSubmissionsDTO;
import com.microservice.task.http.request.student.TaskSubmissionRequest;
import com.microservice.task.http.request.teacher.GradeTaskRequest;

public interface TaskStudentService {
    TaskSubmissionDTO submitTask(Long studentId, Long taskId, TaskSubmissionRequest submissionRequest);
    TaskWithSubmissionsDTO getTaskWithSubmissions(Long taskId);

}
