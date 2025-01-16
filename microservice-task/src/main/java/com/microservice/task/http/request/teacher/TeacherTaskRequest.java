package com.microservice.task.http.request.teacher;

import com.microservice.task.entity.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherTaskRequest {
    private String title;            // Título de la tarea
    private String description;      // Descripción de la tarea
    private TaskType type;

    private Date creationDate;

    private Date startDate;          // Fecha de inicio de la tarea
    private Date endDate;            // Fecha de finalización de la tarea

    private Long teacherId;          // ID del maestro que asigna la tarea
    private Long courseId;           // ID del curso asociado con la tarea

}
