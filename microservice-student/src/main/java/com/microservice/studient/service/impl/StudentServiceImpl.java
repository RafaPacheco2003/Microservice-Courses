package com.microservice.studient.service.impl;

import com.microservice.studient.client.CourseClient;
import com.microservice.studient.dto.CourseDTO;
import com.microservice.studient.entity.Student;
import com.microservice.studient.http.request.StudentRequest;
import com.microservice.studient.persistence.StudentRepository;
import com.microservice.studient.service.CourseService;
import com.microservice.studient.service.StudentService;
import com.microservice.studient.dto.StudentDTO;
import com.microservice.studient.util.validations.ValidationService;
import com.microservice.studient.util.mappers.StudentMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ValidationService validationService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CourseClient courseClient;
    @Autowired
    private CourseService courseService;

    @Override
    public StudentDTO saveStudent(StudentRequest studentRequest) {


        // Convertir StudentRequest a Student (Entidad)
        Student student = studentMapper.CreateRequestToEntity(studentRequest);

        // Guardar el estudiante en la base de datos
        Student savedStudent = studentRepository.save(student);

        // Convertir la entidad guardada a StudentDTO
        StudentDTO savedStudentDTO = studentMapper.EntityToDTO(savedStudent);

        // Asignar el nombre del curso al StudentDTO
        assignCourseNameToDTO(savedStudentDTO);

        // Retornar el StudentDTO
        return savedStudentDTO;
    }


    @Override
    public StudentDTO findById(Long id) {
        // Buscar el estudiante y mapearlo a DTO
        Student student = validationService.validateStudent(id);
        // Mapear el estudiante a DTO
        StudentDTO studentDTO = studentMapper.EntityToDTO(student);

        // Asignar el nombre del curso al DTO usando el metodo helper
        assignCourseNameToDTO(studentDTO);

        return studentDTO;
    }

    @Override
    public StudentDTO updateStudent(Long id, StudentRequest studentRequest) {
        Long courseId = studentRequest.getCourseId();
        //Obtenemos todos los cursos
        CourseDTO course = courseService.getCourseById(courseId);


        // Buscar el estudiante existente por su ID
        Student existingStudent = validationService.validateStudent(id);


        // Mapear el DTO a la entidad existente sin modificar el ID
        modelMapper.map(studentRequest, existingStudent);

        // Guardar el estudiante actualizado en la base de datos
        Student updatedStudent = studentRepository.save(existingStudent);

        // Mapear el estudiante actualizado a DTO
        StudentDTO updatedStudentDTO = modelMapper.map(updatedStudent, StudentDTO.class);

        // Asignar el nombre del curso al DTO actualizado
        assignCourseNameToDTO(updatedStudentDTO);

        return updatedStudentDTO;
    }

    @Override
    public List<StudentDTO> findAll() {
        List<Student> students = (List<Student>) studentRepository.findAll();

        // Mapeamos los estudiantes a DTOs
        List<StudentDTO> studentDTOs = students.stream().map(student -> {
            // Mapear a StudentDTO
            StudentDTO studentDTO = modelMapper.map(student, StudentDTO.class);

            // Asignar el nombre del curso al StudentDTO usando el método helper
            assignCourseNameToDTO(studentDTO);

            return studentDTO;
        }).collect(Collectors.toList());

        return studentDTOs;
    }



    @Override
    public void deleteById(Long id) {
        // Busca el estudiante y lanza una excepción si no existe
        Student student = validationService.validateStudent(id);
        // Si el estudiante existe, lo eliminamos por su id
        studentRepository.deleteById(id);
    }

    /*
    Comunicaciones con el course
     */
    @Override
    public List<StudentDTO> findByIdCourse(Long idCourse) {
        // Obtener estudiantes por curso y mapearlos a DTO
        List<Student> students = studentRepository.findAllStudents(idCourse);
        return students.stream()
                .map(student -> modelMapper.map(student, StudentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void updateStudentsCourseId(Long idCourse) {
        // Actualizar a todos los estudiantes que tienen este courseId y ponerlo a 0
        List<Student> students = studentRepository.findAllStudents(idCourse);
        students.forEach(student -> student.setCourseId(null));
        studentRepository.saveAll(students);
    }





    // Metodo auxiliar para asignar el nombre del curso al StudentDTO
    private void assignCourseNameToDTO(StudentDTO studentDTO) {
        Long courseId = studentDTO.getCourseId();
        CourseDTO course = null;
        try {
            course = courseClient.getCourseById(courseId);
        } catch (Exception e) {
            // Manejar error si no se puede obtener el curso
            System.out.println("Error obteniendo el curso para el estudiante con ID: " + studentDTO.getId());
        }

        // Asignar el nombre del curso al StudentDTO
        if (course != null) {
            studentDTO.setCourseName(course.getName());
        } else {
            studentDTO.setCourseName("Course not found");
        }
    }

}
