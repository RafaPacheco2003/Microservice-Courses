package com.microservice.task.util.validations;

import com.microservice.task.entity.Task;
import com.microservice.task.entity.TaskSubmission;
import com.microservice.task.service.StudentService;

import java.util.Date;

public interface TaskValidatorService {

    Task validateTaskExists(Long taskId);

    TaskSubmission validateSubmissionExists(Long submissionId);

    void verifyStudentEnrolledInCourse(Long studentId, Task task, StudentService studentService);

    boolean isLate(Date taskEndDate, Date submissionDate);
}
