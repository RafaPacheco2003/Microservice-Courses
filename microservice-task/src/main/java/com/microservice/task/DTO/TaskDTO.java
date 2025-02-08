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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDTO {
    private Long id;

    private String title;
    private String description;
    private TaskType type;  // Enum for the task type (EXAM, ASSIGNMENT)
    private Date creationDate; // Date when the task was created
    private Date startDate;  // Date when the task starts
    private Date endDate;  // Date when the task is due


    //private TaskStatus status;  // Enum for the task status (DELIVERED, GRADED, RETURNED)


    private Date updatedDate;  // Date when the task was last updated

    private Long teacherId;  // ID of the teacher
    private String teacherName;  // Name of the teacher
    private Long courseId;  // ID of the course
    private String courseName;  // Name of the course

    private List<Long> studentIds;  // List of student IDs
}
