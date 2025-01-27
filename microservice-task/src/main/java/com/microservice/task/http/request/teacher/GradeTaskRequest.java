package com.microservice.task.http.request.teacher;

import lombok.Data;

@Data
public class GradeTaskRequest {
    private Double grade;
    private String teacherComment;
}
