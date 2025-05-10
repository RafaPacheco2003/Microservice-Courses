package com.microservice.task.http.request.student;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

@Data
public class TaskSubmissionRequest {
    private String studentComment;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date submissionDate;  // Ahora acepta formato ISO directamente

    private transient MultipartFile pdfFile;  // transient para que Jackson lo ignore
}