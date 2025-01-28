package com.microservice.task.util.mappers.impl;

import com.microservice.task.DTO.TaskSubmissionDTO;
import com.microservice.task.entity.TaskSubmission;
import com.microservice.task.util.mappers.TaskSubmissionMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskSubmissionMapperImpl implements TaskSubmissionMapper {

    @Override
    public TaskSubmissionDTO convertToDTO(TaskSubmission taskSubmission) {
        TaskSubmissionDTO dto = new TaskSubmissionDTO();
        dto.setId(taskSubmission.getId());
        dto.setStudentId(taskSubmission.getStudentId());
        dto.setSubmitted(taskSubmission.isSubmitted());
        dto.setLate(taskSubmission.isLate());
        dto.setStudentComment(taskSubmission.getStudentComment());
        dto.setGrade(taskSubmission.getGrade());
        dto.setTeacherComment(taskSubmission.getTeacherComment());
        dto.setSubmissionDate(taskSubmission.getSubmissionDate());
        dto.setTaskTitle(taskSubmission.getTask().getTitle());
        dto.setSubmittedPdfFilePath(taskSubmission.getPdfFile());
        return dto;
    }

    @Override
    public List<TaskSubmissionDTO> convertToDTOList(List<TaskSubmission> taskSubmissions) {
        return taskSubmissions.stream()
                .map(this::convertToDTO)
                .toList();
    }
}

