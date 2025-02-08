package com.microservice.task.util.mappers.impl;

import com.microservice.task.DTO.TaskSubmissionDTO;
import com.microservice.task.entity.Task;
import com.microservice.task.entity.TaskSubmission;
import com.microservice.task.http.request.student.TaskSubmissionRequest;
import com.microservice.task.util.mappers.TaskSubmissionMapper;
import com.microservice.task.util.validations.TaskValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskSubmissionMapperImpl implements TaskSubmissionMapper {

    @Autowired
    private TaskValidatorService taskValidator;

    @Override
    public TaskSubmissionDTO convertTaskSubmissionToTaskSubmissionDTO(TaskSubmission taskSubmission) {
        TaskSubmissionDTO dto = new TaskSubmissionDTO();
        dto.setId(taskSubmission.getId());
        dto.setStudentId(taskSubmission.getStudentId());
        dto.setSubmitted(taskSubmission.isSubmitted());
        dto.setStudentComment(taskSubmission.getStudentComment());
        dto.setGrade(taskSubmission.getGrade());
        dto.setGradeDate(taskSubmission.getGradeDate());
        dto.setTeacherComment(taskSubmission.getTeacherComment());
        dto.setSubmissionDate(taskSubmission.getSubmissionDate());
        dto.setSubmittedPdfFilePath(taskSubmission.getPdfFile());
        return dto;
    }

    @Override
    public List<TaskSubmissionDTO> convertListTaskSubmissionToListTaskSubmissionDTO(List<TaskSubmission> taskSubmissions) {
        return taskSubmissions.stream()
                .map(this::convertTaskSubmissionToTaskSubmissionDTO)
                .toList();
    }

    @Override
    public TaskSubmission convertTaskSubmissionRequestToEntity(Long studentId, TaskSubmissionRequest submissionRequest, Task task, String storedFilePath) {
        TaskSubmission taskSubmission = new TaskSubmission();
        taskSubmission.setStudentId(studentId);
        taskSubmission.setSubmitted(true);
        taskSubmission.setStudentComment(submissionRequest.getStudentComment());
        taskSubmission.setLate(taskValidator.isLate(task.getEndDate(), submissionRequest.getSubmissionDate()));
        taskSubmission.setSubmissionDate(submissionRequest.getSubmissionDate());
        taskSubmission.setPdfFile(storedFilePath);  // Guardar la ruta del archivo en la base de datos
        taskSubmission.setTask(task);
        return taskSubmission;
    }
}

