package com.microservice.task.util.assignments;

import com.microservice.task.DTO.CourseDTO;
import com.microservice.task.DTO.TaskDTO;
import com.microservice.task.DTO.TeacherDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AssignCourseAndTeacherNamesToDTO {
    public void assignCourseAndTeacherNamesToDTO(TaskDTO taskDTO, CourseDTO course, TeacherDTO teacher) {
        String courseName = Optional.ofNullable(course)
                .map(CourseDTO::getName)
                .orElse("Course not found");
        taskDTO.setCourseName(courseName);

        String teacherName = Optional.ofNullable(teacher)
                .map(TeacherDTO::getName)
                .orElse("Teacher not found");
        taskDTO.setTeacherName(teacherName);
    }
}
