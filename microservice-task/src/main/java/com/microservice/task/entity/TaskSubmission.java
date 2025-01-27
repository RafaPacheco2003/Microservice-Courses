package com.microservice.task.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;   // ID del estudiante que entrega la tarea
    private boolean submitted; // Indica si el estudiante entregó la tarea
    private boolean isLate;    // Indica si la tarea fue entregada tarde
    private String studentComment; // Comentario del estudiante

    private Double grade;  // Calificación dada por el profesor
    private Date gradeDate;
    private String teacherComment;  // Comentario del profesor

    private Date submissionDate;  // Fecha de entrega

    // Relación ManyToOne con Task, ya que varias entregas pertenecen a una sola tarea
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;


    private String pdfFile; // Almacena el archivo PDF como un arreglo de bytes
}
