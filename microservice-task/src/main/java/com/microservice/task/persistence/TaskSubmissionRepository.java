package com.microservice.task.persistence;

import com.microservice.task.entity.TaskSubmission;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskSubmissionRepository extends CrudRepository<TaskSubmission, Long> {
    // Método para encontrar todas las entregas por el ID de la tarea
    List<TaskSubmission> findByTaskId(Long taskId);


}
