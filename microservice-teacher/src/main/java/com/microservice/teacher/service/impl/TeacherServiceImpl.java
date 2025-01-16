package com.microservice.teacher.service.impl;

import com.microservice.teacher.client.CourseClient;
import com.microservice.teacher.dto.CourseDTO;
import com.microservice.teacher.dto.TeacherDTO;
import com.microservice.teacher.entity.Teacher;
import com.microservice.teacher.http.response.CourseByTeacherResponse;
import com.microservice.teacher.persistence.TeacherRepository;
import com.microservice.teacher.service.TeacherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private CourseClient courseClient;
    @Autowired
    private TeacherRepository teacherRepository;


    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TeacherDTO addTeacher(TeacherDTO teacherDTO) {
        Teacher teacher = modelMapper.map(teacherDTO, Teacher.class);
        Teacher savedTeacher = teacherRepository.save(teacher);
        return modelMapper.map(savedTeacher, TeacherDTO.class);
    }

    @Override
    public TeacherDTO getTeacherById(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));
        return modelMapper.map(teacher, TeacherDTO.class);
    }

    @Override
    public List<TeacherDTO> getAllTeachers() {
        return  ((List<Teacher>) teacherRepository.findAll())
                .stream()
                .map(teacher -> modelMapper.map(teacher, TeacherDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTeacher(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(()-> new RuntimeException("Teacher not found with id: " + teacherId));
        teacherRepository.delete(teacher);

        courseClient.updateCourseByTeacherId(teacherId);
    }




    @Override
    public CourseByTeacherResponse findCourseByTeacherId(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElse(null);
        List<CourseDTO> courseDTOList = courseClient.findCourseByTeacherId(teacherId);

        return CourseByTeacherResponse.builder()
                .nameTeacher(teacher.getName())
                .courseDTOList(courseDTOList)
                .build();

    }
}
