package com.microservice.task.http.request.teacher;

import com.microservice.task.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherTaskGradeRequest {

    private Double grade;  // The grade for the task
    private String teacherComment;  // Comment from the teacher when grading the task
    private Date updatedDate;
}
