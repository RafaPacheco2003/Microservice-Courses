package com.microservice.task.DTO;

import com.microservice.task.entity.TaskStatus;
import com.microservice.task.entity.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskWithSubmissionsDTO {
    private Long taskId;
    private String title;
    private String description;
    private TaskType type;
    private Date creationDate;
    private Date startDate;
    private Date endDate;

    //private TaskStatus status;
    //private Double grade;
    private Long teacherId;
    private String teacherName;
    private Long courseId;
    private String courseName;
    private List<TaskSubmissionDTO> submissions; // Lista de entregas asociadas a la tarea
}
