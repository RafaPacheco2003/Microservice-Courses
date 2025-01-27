package com.microservice.task.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TaskType type;  // Enum para el estado de la tarea
    private Date creationDate; // Date when the task was created
    private Date startDate;
    private Date endDate;



    //agregar fecha de entrega
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;  // Enum para el estado de la tarea



    private String teacherComment; // Additional comments or notes about the task
    private Date updatedDate; // Last update date of the task



    @Column(name = "teacher_id")
    private Long teacherId;
    @Column(name = "course_id")
    private Long courseId;

    // Relación uno a muchos con las entregas de los estudiantes
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TaskSubmission> taskSubmissions = new ArrayList<>();  // Lista de entregas asociadas a la tarea

    // Inicializar como una lista vacía
    @ElementCollection
    private List<Long> studentIds =  new ArrayList<>();  // Relación lógica de IDs de estudiantes, no mapeada en la base de datos


}
