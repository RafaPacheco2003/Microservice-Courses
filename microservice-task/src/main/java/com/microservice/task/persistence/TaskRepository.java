package com.microservice.task.persistence;

import com.microservice.task.entity.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {

    List<Task> findAllByTeacherId(Long teacherId);
    List<Task> findAllByCourseId(Long courseId);
    List<Task>findByCourseId(Long courseId);
    List<Task> findAllByStudentIds(Long studentIds);
    //List<Task> findByStudentIds(Long studentIds);

}
