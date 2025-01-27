package com.microservice.task.service.impl;

import com.microservice.task.entity.Task;
import com.microservice.task.entity.TaskSubmission;
import com.microservice.task.persistence.TaskRepository;
import com.microservice.task.persistence.TaskSubmissionRepository;
import com.microservice.task.service.StudentService;
import com.microservice.task.service.TaskValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskValidatorServiceImpl implements TaskValidatorService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskSubmissionRepository taskSubmissionRepository;

    @Override
    public Task validateTaskExists(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada con el ID: " + taskId));
    }

    @Override
    public TaskSubmission validateSubmissionExists(Long submissionId) {
        return taskSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Entrega de tarea no encontrada con el ID: " + submissionId));
    }

    @Override
    public void verifyStudentEnrolledInCourse(Long studentId, Task task, StudentService studentService) {
        List<Long> studentIds = studentService.getStudentIdsByCourse(task.getCourseId());
        if (!studentIds.contains(studentId)) {
            throw new IllegalArgumentException("El estudiante no está inscrito en el curso de esta tarea.");
        }
    }

    @Override
    public boolean isLate(Date taskEndDate, Date submissionDate) {
        return submissionDate.after(taskEndDate);
    }
}

