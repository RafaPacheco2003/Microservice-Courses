package com.microservice.teacher.service;

import com.microservice.teacher.dto.TeacherDTO;
import com.microservice.teacher.http.response.CourseByTeacherResponse;

import java.util.List;

public interface TeacherService {
    TeacherDTO addTeacher(TeacherDTO teacherDTO);
    TeacherDTO getTeacherById(Long teacherId);
    List<TeacherDTO> getAllTeachers();
    void deleteTeacher(Long teacherId);

    CourseByTeacherResponse findCourseByTeacherId(Long teacherId);
}
