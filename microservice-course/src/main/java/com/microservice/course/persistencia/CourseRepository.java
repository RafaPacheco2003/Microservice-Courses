package com.microservice.course.persistencia;

import com.microservice.course.entity.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {

    List<Course>  findAllByTeacherId(Long teacherId);
}
