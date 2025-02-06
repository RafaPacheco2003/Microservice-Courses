package com.microservice.task.http.request.teacher;

import lombok.Data;

import java.util.Date;

@Data
public class GradeTaskRequest {
    private Double grade;
    private Date gradeDate;
    private String teacherComment;
}
