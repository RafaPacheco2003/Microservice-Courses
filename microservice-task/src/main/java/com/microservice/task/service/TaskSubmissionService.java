package com.microservice.task.service;

import com.microservice.task.DTO.TaskSubmissionDTO;
import com.microservice.task.DTO.TaskWithSubmissionsDTO;
import com.microservice.task.http.request.student.TaskSubmissionRequest;
import com.microservice.task.http.request.teacher.GradeTaskRequest;

public interface TaskSubmissionService {
    TaskSubmissionDTO submitTaskOnce(Long studentId, Long taskId, TaskSubmissionRequest submissionRequest);
    TaskSubmissionDTO submitTask(Long studentId, Long taskId, TaskSubmissionRequest submissionRequest);
    TaskWithSubmissionsDTO getTaskWithSubmissions(Long taskId);
    TaskSubmissionDTO gradeTaskSubmission(Long teacherId, Long submissionId, GradeTaskRequest gradeRequest);
    TaskWithSubmissionsDTO getTaskWithSubmissionsForStudent(Long studentId, Long taskId);
}
