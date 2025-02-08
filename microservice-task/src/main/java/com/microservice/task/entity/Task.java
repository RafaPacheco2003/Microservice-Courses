package com.microservice.task.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "dd/MM/yyyy")  // Formato al serializar la fecha
    private Date creationDate; // Date when the task was created
    @JsonFormat(pattern = "dd/MM/yyyy")  // Formato al serializar la fecha
    private Date startDate;
    @JsonFormat(pattern = "dd/MM/yyyy")  // Formato al serializar la fecha
    private Date endDate;
    @JsonFormat(pattern = "dd/MM/yyyy")  // Formato al serializar la fecha
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



    // Este método se ejecutará antes de que la entidad sea persistida
    @PrePersist
    public void prePersist() {
        if (this.creationDate == null) {
            this.creationDate = new Date();  // Establecer la fecha actual
        }

        // Validación de fechas
        if (this.startDate != null && this.creationDate != null && this.startDate.before(this.creationDate)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser menor que la fecha de creación.");
        }

        if (this.endDate != null && this.startDate != null && this.endDate.before(this.startDate)) {
            throw new IllegalArgumentException("La fecha de finalización no puede ser menor que la fecha de inicio.");
        }
    }
}
