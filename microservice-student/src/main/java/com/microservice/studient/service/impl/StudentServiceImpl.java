package com.microservice.studient.service.impl;

import com.microservice.studient.client.CourseClient;
import com.microservice.studient.dto.CourseDTO;
import com.microservice.studient.dto.TeacherDTO;
import com.microservice.studient.entity.Student;
import com.microservice.studient.exception.StudentNotFoundException;
import com.microservice.studient.http.request.StudentRequest;
import com.microservice.studient.http.response.StudentDetailsDTO;
import com.microservice.studient.persistence.StudentRepository;
import com.microservice.studient.service.CourseService;
import com.microservice.studient.service.StudentService;
import com.microservice.studient.dto.StudentDTO;
import com.microservice.studient.service.TeacherService;
import com.microservice.studient.util.assignments.AssignCourseNameToDTO;
import com.microservice.studient.util.validations.ValidationService;
import com.microservice.studient.util.mappers.StudentMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the StudentService interface that handles the business logic
 * related to the Student entity.
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private AssignCourseNameToDTO assignCourseNameToDTO;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CourseService courseService;
    @Autowired
    private TeacherService teacherService;

    /**
     * Saves a new student by converting the StudentRequest to a Student entity,
     * saving it in the database, and returning a StudentDTO with course information.
     *
     * @param studentRequest the student request data
     * @return the saved student as a StudentDTO
     */
    @Override
    public StudentDTO saveStudent(StudentRequest studentRequest) {
        // Convert StudentRequest to Student entity
        Student student = studentMapper.CreateRequestToEntity(studentRequest);

        // Save the student to the database
        Student savedStudent = studentRepository.save(student);

        // Convert the saved entity to StudentDTO
        StudentDTO savedStudentDTO = studentMapper.EntityToDTO(savedStudent);

        // Assign course name to the StudentDTO
        assignCourseNameToDTO.assignCourseNameToDTO(savedStudentDTO);

        // Return the DTO with course information
        return savedStudentDTO;
    }

    /**
     * Finds a student by its ID, validates its existence, and returns a StudentDTO
     * with course information.
     *
     * @param id the student ID
     * @return the student as a StudentDTO
     */
    @Override
    public StudentDTO findById(Long id) {
        // Validate and retrieve the student by ID
        Student student = validationService.validateStudent(id);

        // Map the student entity to StudentDTO
        StudentDTO studentDTO = studentMapper.EntityToDTO(student);

        // Assign course name to the DTO
        assignCourseNameToDTO.assignCourseNameToDTO(studentDTO);

        // Return the DTO
        return studentDTO;
    }

    /**
     * Updates an existing student based on the provided ID and request data. The
     * student is validated, updated, and saved. The updated student is returned as
     * a StudentDTO with course information.
     *
     * @param id the student ID
     * @param studentRequest the updated student data
     * @return the updated student as a StudentDTO
     */
    @Override
    public StudentDTO updateStudent(Long id, StudentRequest studentRequest) {
        // Retrieve course information based on course ID
        CourseDTO course = courseService.getCourseById(studentRequest.getCourseId());

        // Validate and retrieve the existing student by ID
        Student existingStudent = validationService.validateStudent(id);

        // Update the existing student entity using the request data
        studentMapper.UpdateRequestToEntity(studentRequest, existingStudent);

        // Save the updated student to the database
        Student updatedStudent = studentRepository.save(existingStudent);

        // Map the updated entity to StudentDTO
        StudentDTO updatedStudentDTO = studentMapper.EntityToDTO(updatedStudent);

        // Assign course name to the updated DTO
        assignCourseNameToDTO.assignCourseNameToDTO(updatedStudentDTO);

        // Return the updated DTO
        return updatedStudentDTO;
    }

    /**
     * Retrieves all students, maps them to DTOs, and assigns the course name to each.
     *
     * @return a list of students as StudentDTOs
     */
    @Override
    public List<StudentDTO> findAll() {
        // Retrieve all students from the repository
        List<Student> students = (List<Student>) studentRepository.findAll();

        // Map each student entity to StudentDTO and assign course names
        List<StudentDTO> studentDTOs = students.stream()
                .map(student -> {
                    // Convert to StudentDTO
                    StudentDTO studentDTO = studentMapper.EntityToDTO(student);

                    // Assign course name
                    assignCourseNameToDTO.assignCourseNameToDTO(studentDTO);

                    return studentDTO;
                }).collect(Collectors.toList());

        // Return the list of DTOs
        return studentDTOs;
    }

    /**
     * Deletes a student by its ID after validating its existence.
     *
     * @param id the student ID
     */
    @Override
    public void deleteById(Long id) {
        // Validate and retrieve the student by ID
        Student student = validationService.validateStudent(id);

        // Delete the student by its ID
        studentRepository.deleteById(id);
    }

    /**
     * Finds all students by a specific course ID and maps them to StudentDTOs.
     *
     * @param idCourse the course ID
     * @return a list of students as StudentDTOs
     */
    @Override
    public List<StudentDTO> findByIdCourse(Long idCourse) {
        // Retrieve all students by course ID
        List<Student> students = studentRepository.findAllStudents(idCourse);

        // Map each student entity to StudentDTO
        return students.stream()
                .map(student -> modelMapper.map(student, StudentDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Updates all students that are associated with a given course ID by setting
     * the course ID to null (removing the association).
     *
     * @param idCourse the course ID to disassociate from students
     */
    @Override
    public void updateStudentsCourseId(Long idCourse) {
        // Retrieve all students by course ID
        List<Student> students = studentRepository.findAllStudents(idCourse);

        // Set course ID to null for each student
        students.forEach(student -> student.setCourseId(null));

        // Save the updated students
        studentRepository.saveAll(students);
    }

    @Override
    public StudentDetailsDTO findStudentDetailsById(Long id) {

        Student student = validationService.validateStudent(id);
        CourseDTO course = courseService.getCourseById(student.getCourseId());
        TeacherDTO teacher = teacherService.getTeacherById(course.getId());

        return StudentDetailsDTO.builder()
                .id(student.getId())
                .name(student.getName() + " " + student.getLastName())
                .courseId(course.getId())
                .email(student.getEmail())
                .courseName(course.getName())
                .teacher(teacher)
                .build();

    }
}
