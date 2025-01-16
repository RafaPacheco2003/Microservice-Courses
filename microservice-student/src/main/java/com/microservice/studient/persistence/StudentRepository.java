package com.microservice.studient.persistence;

import com.microservice.studient.entity.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {

    @Query("SELECT s FROM Student s WHERE s.courseId = :idCourse")
    List<Student> findAllStudents(Long idCourse);
    List<Student> findAllByCourseId(Long courseId);//Es lo mismo que el de arriba

}
