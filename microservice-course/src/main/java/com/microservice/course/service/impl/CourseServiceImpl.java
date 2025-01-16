package com.microservice.course.service.impl;

import com.microservice.course.client.StudentClient;
import com.microservice.course.client.TeacherClient;
import com.microservice.course.dto.CourseDTO;
import com.microservice.course.dto.StudentDTO;
import com.microservice.course.dto.TeacherDTO;
import com.microservice.course.entity.Course;
import com.microservice.course.http.response.StudentByCourseResponse;
import com.microservice.course.persistencia.CourseRepository;
import com.microservice.course.service.CourseService;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentClient studentClient;
    @Autowired
    private TeacherClient teacherClient;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CourseDTO addCourse(CourseDTO courseDTO) {
        Long teacherId = courseDTO.getTeacherId();
        TeacherDTO teacher = null;
        try {
            teacher = teacherClient.getTeacher(teacherId);
        }catch (Exception e) {
            throw new RuntimeException("A Teacher with ID " + teacherId + " not found", e);
        }

        // Convertir CourseDTO a Course, guardar y devolver como CourseDTO
        Course course = modelMapper.map(courseDTO, Course.class);
        Course savedCourse = courseRepository.save(course);
        CourseDTO savedCourseDTO = modelMapper.map(savedCourse, CourseDTO.class);

        assignTeacherNameToDTO(savedCourseDTO);

        return savedCourseDTO;
    }

    @Override
    public CourseDTO getCourseById(Long courseId) {
        // Convertir Course a CourseDTO
        CourseDTO courseDTO = courseRepository.findById(courseId)
                .map(course -> modelMapper.map(course, CourseDTO.class))
                .orElseThrow(() -> new RuntimeException("No se encontró el curso con ID " + courseId));

        // Asignar el nombre del profesor al DTO
        assignTeacherNameToDTO(courseDTO);

        return courseDTO;
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        // Convertir la lista de Course a CourseDTO
        List<CourseDTO> courseDTOs = ((List<Course>) courseRepository.findAll())
                .stream()
                .map(course -> modelMapper.map(course, CourseDTO.class))
                .collect(Collectors.toList());

        // Asignar el nombre del profesor a cada DTO
        courseDTOs.forEach(this::assignTeacherNameToDTO);

        return courseDTOs;
    }


    @Override
    public void deleteCourseById(Long courseId) {
        // Verificar si el curso existe antes de eliminarlo
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        // Eliminar el curso
        courseRepository.deleteById(courseId);

        // Actualizar los estudiantes del microservicio Student para poner courseId en null o 0
        studentClient.updateStudentsCourseId(courseId);
    }

    //Comunicacion con el teacher
    @Override
    public List<CourseDTO> findByTeacherId(Long teacherId) {
        List<Course> courses = courseRepository.findAllByTeacherId(teacherId);

        return courses.stream()
                .map(course -> modelMapper.map(course, CourseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void updateCourseByTeacherId(Long teacherId) {
        List<Course> courses = courseRepository.findAllByTeacherId(teacherId);
        courses.forEach(course -> course.setTeacherId(null));
        courseRepository.saveAll(courses);
    }


    //Service de comunicacion con student
    @Override
    public StudentByCourseResponse findStudentByIdCoourse(Long idCourse) {
        //Consultar el curso
        Course course = courseRepository.findById(idCourse).orElseThrow();

        //Obtener los estudiantes
        List<StudentDTO> studentDTOList = studentClient.findAllStudentsByCourse(idCourse);

        return StudentByCourseResponse.builder()
                .courseName(course.getName())
                .teacher(course.getTeacher())
                .studentDTOList(studentDTOList)
                .build();
    }



    private void assignTeacherNameToDTO(CourseDTO courseDTO) {
        // Verificar si el ID del profesor es nulo
        if (courseDTO.getTeacherId() == null) {
            courseDTO.setTeacherName("No se encontró teacher");
        } else {
            try {
                // Intentar obtener el nombre del profesor desde el servicio
                TeacherDTO teacher = teacherClient.getTeacher(courseDTO.getTeacherId());
                if (teacher != null) {
                    courseDTO.setTeacherName(teacher.getName());
                } else {
                    courseDTO.setTeacherName("No se encontró teacher");
                }
            } catch (Exception e) {
                // En caso de error al obtener el profesor, asignar un nombre predeterminado
                courseDTO.setTeacherName("No se encontró teacher");
            }
        }
    }







}
