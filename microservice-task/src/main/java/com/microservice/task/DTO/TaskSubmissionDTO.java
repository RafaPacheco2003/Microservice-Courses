package com.microservice.task.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class TaskSubmissionDTO {
    private Long id;
    private Long studentId;

    private Date submissionDate;
    private boolean submitted;
    private boolean isLate;
    private String studentComment;
    private String submittedPdfFilePath;

    private Double grade;
    private Date gradeDate; //Date grade
    private String teacherComment;





    // Puedes agregar más detalles según necesites
}
