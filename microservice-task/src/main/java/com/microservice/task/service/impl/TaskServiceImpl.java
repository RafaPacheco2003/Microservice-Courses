package com.microservice.task.service.impl;

import com.microservice.task.DTO.CourseDTO;
import com.microservice.task.DTO.StudentDTO;
import com.microservice.task.DTO.TaskDTO;
import com.microservice.task.DTO.TeacherDTO;
import com.microservice.task.client.CourseClient;
import com.microservice.task.client.StudentClient;
import com.microservice.task.client.TeacherClient;
import com.microservice.task.entity.Task;
import com.microservice.task.entity.TaskStatus;
import com.microservice.task.exception.TaskNotFoundException;
import com.microservice.task.http.request.teacher.TeacherTaskGradeRequest;
import com.microservice.task.http.request.teacher.TeacherTaskRequest;
import com.microservice.task.persistence.TaskRepository;
import com.microservice.task.service.TaskService;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    // Crear la instancia de ModelMapper dentro del servicio
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private StudentClient studentClient;
    @Autowired
    private CourseClient courseClient;
    @Autowired
    private TeacherClient teacherClient; // Asegúrate de inyectar el cliente de TeacherClient


    @Override
    public TaskDTO addTask(TeacherTaskRequest taskRequest) {
        // Obtener el curso y el profesor
        CourseDTO course = getCourseById(taskRequest.getCourseId());
        TeacherDTO teacher = getTeacherById(taskRequest.getTeacherId());

        // Convertir el request a tarea
        Task task = convertRequestToTask(taskRequest);

        // Obtener la lista de IDs de estudiantes por curso
        List<Long> studentIds = getStudentIdsByCourse(taskRequest.getCourseId());

        // Asignar los IDs de estudiantes a la tarea (solo lógicamente)
        task.setStudentIds(studentIds);

        // Guardar la tarea en la base de datos
        Task savedTask = taskRepository.save(task);

        // Convertir la tarea guardada a DTO
        TaskDTO taskDTO = convertTaskToDTO(savedTask);

        // Asignar el nombre del curso y del profesor
        assignCourseAndTeacherNamesToDTO(taskDTO, course, teacher);

        return taskDTO;
    }

    @Override
    public TaskDTO updateTask(Long id, TeacherTaskRequest taskRequest) {
        // Buscar la tarea por ID
        Task task = getTaskByIdWithException(id);

        // Convertir el request en una tarea y actualizarla
        Task updatedTask = prepareUpdatedTask(id, taskRequest);

        // Obtener la lista de estudiantes y asignarles IDs
        List<Long> studentIds = getStudentIdsByCourse(taskRequest.getCourseId());

        // Asignar los IDs de estudiantes a la tarea (solo lógicamente)
        updatedTask.setStudentIds(studentIds);

        // Guardar la tarea actualizada en la base de datos
        Task savedTask = taskRepository.save(updatedTask);

        // Convertir la tarea guardada a DTO y devolverlo
        return convertTaskToDTO(savedTask);
    }

    @Override
    public TaskDTO gradeTask(Long id, TeacherTaskGradeRequest gradeRequest) {
        // Buscar la tarea por ID
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));


        // Mapear el gradeRequest a la tarea existente usando ModelMapper
        modelMapper.map(gradeRequest, task);
        if (task.getGrade() >= 70){
            task.setStatus(TaskStatus.GRADED);
        }else{
            task.setStatus(TaskStatus.RETURNED);
        }
        // Establecer la fecha de actualización y asegurarse de que el status sea el adecuado
        task.setUpdatedDate(new Date()); // Actualizar la fecha de la última modificación

        // Guardar la tarea actualizada en la base de datos
        Task savedTask = taskRepository.save(task);

        // Convertir la tarea guardada a DTO y retornarla
        return convertTaskToDTO(savedTask);
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        // Obtener el profesor y el curso
        TeacherDTO teacher = teacherClient.searchTeacher(task.getTeacherId());
        CourseDTO course = courseClient.searchCourse(task.getCourseId());

        // Obtener estudiantes asociados a esta tarea
        List<StudentDTO> students = studentClient.findByIdCourse(task.getCourseId());
        List<Long> studentIds = students.stream()
                .map(StudentDTO::getId)
                .collect(Collectors.toList());
        task.setStudentIds(studentIds);

        // Convertir la tarea a DTO y asignar los IDs de estudiantes
        TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);
        taskDTO.setStudentIds(studentIds);

        // Asignar el nombre del curso y del profesor
        assignCourseAndTeacherNamesToDTO(taskDTO, course, teacher);

        return taskDTO;
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.findById(id)
                .ifPresentOrElse(taskRepository::delete,
                        () -> { throw new TaskNotFoundException(id); });
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        // Obtener todas las tareas desde el repositorio
        List<Task> tasks = (List<Task>) taskRepository.findAll();

        // Convertir la lista de tareas a una lista de TaskDTO
        return tasks.stream()
                .map(task -> {
                    // Obtener estudiantes asociados a esta tarea usando el listado de IDs
                    List<StudentDTO> students = studentClient.findByIdCourse(task.getCourseId());
                    List<Long> studentIds = students.stream()
                            .map(StudentDTO::getId)
                            .collect(Collectors.toList());

                    // Asignar los IDs de estudiantes a la tarea
                    task.setStudentIds(studentIds);

                    // Convertir la tarea a DTO
                    TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);
                    taskDTO.setStudentIds(studentIds);  // Asignar los IDs de estudiantes en el DTO
                    return taskDTO;
                })
                .collect(Collectors.toList());  // Recoger los resultados en una lista
    }



    @Override
    public List<TaskDTO> getTasksByCourse(Long courseId) {
        // Verificar si el curso existe usando el CourseClient
        CourseDTO courseDTO = courseClient.searchCourse(courseId);
        if (courseDTO == null) {
            throw new RuntimeException("Course with Id: " + courseId + " not found");
        }

        // Obtener todas las tareas relacionadas con el curso dado
        List<Task> tasks = taskRepository.findByCourseId(courseId);

        // Obtener estudiantes asociados al curso
        List<StudentDTO> students = studentClient.findByIdCourse(courseId);
        List<Long> studentIds = students.stream()
                .map(StudentDTO::getId)
                .collect(Collectors.toList());

        // Convertir la lista de tareas a una lista de TaskDTO usando ModelMapper
        return tasks.stream()
                .map(task -> {
                    TaskDTO dto = modelMapper.map(task, TaskDTO.class);
                    dto.setStudentIds(studentIds);  // Asignar los IDs de estudiantes
                    return dto;
                })
                .collect(Collectors.toList());
    }



    public List<TaskDTO> getTasksByStudentId(Long studentId) {

        StudentDTO studentDTO = studentClient.searchStudent(studentId);
        if (studentDTO == null) {
            throw new RuntimeException("Student With Id: " + studentId + " not found");
        }

        List<Task> tasks = taskRepository.findAllByStudentIds(studentId);

        // Conversión de Task a TaskDTO usando ModelMapper
        return tasks.stream()
                .map(this::convertTaskToDTO)
                .collect(Collectors.toList());
    }





    // Método auxiliar para obtener la tarea por ID
    private Task getTaskByIdWithException(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    // Método auxiliar para preparar la tarea actualizada
    private Task prepareUpdatedTask(Long id, TeacherTaskRequest taskRequest) {
        Task updatedTask = convertRequestToTask(taskRequest);
        updatedTask.setId(id);
        updatedTask.setCreationDate(new Date());
        return updatedTask;
    }

    // Método auxiliar para obtener la lista de IDs de estudiantes
    private List<Long> getStudentIdsByCourse(Long courseId) {
        List<StudentDTO> studentDTOList = studentClient.findByIdCourse(courseId);
        return studentDTOList.stream()
                .map(StudentDTO::getId)  // Extraer el ID de cada estudiante
                .collect(Collectors.toList());  // Recolectar los resultados en una lista
    }
    private CourseDTO getCourseById(Long courseId) {
        CourseDTO course = courseClient.searchCourse(courseId);
        if (course == null) {
            throw new TaskNotFoundException("Course not found");
        }
        return course;
    }

    private TeacherDTO getTeacherById(Long teacherId) {
        TeacherDTO teacher = teacherClient.searchTeacher(teacherId);
        if (teacher == null) {
            throw new TaskNotFoundException("Teacher not found");
        }
        return teacher;
    }

    private List<StudentDTO> getStudentsByCourseId(Long courseId) {
        return studentClient.findByIdCourse(courseId);
    }

    private void assignCourseAndTeacherNamesToDTO(TaskDTO taskDTO, CourseDTO course, TeacherDTO teacher) {
        // Asignar el nombre del curso
        String courseName = Optional.ofNullable(course)
                .map(CourseDTO::getName)
                .orElse("Course not found");
        taskDTO.setCourseName(courseName);

        // Asignar el nombre del profesor
        String teacherName = Optional.ofNullable(teacher)
                .map(TeacherDTO::getName)
                .orElse("Teacher not found");
        taskDTO.setTeacherName(teacherName); // Asegúrate de que TaskDTO tenga el campo teacherName
    }

    // Conversión de TaskRequest a Task
    private Task convertRequestToTask(TeacherTaskRequest taskRequest) {
        Task task = new Task();

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setType(taskRequest.getType());
        task.setCreationDate(taskRequest.getCreationDate());
        task.setStartDate(taskRequest.getStartDate());
        task.setEndDate(taskRequest.getEndDate());
        task.setTeacherId(taskRequest.getTeacherId());
        task.setCourseId(taskRequest.getCourseId());
        return task;
    }

    // Conversión de Task a TaskDTO
    private TaskDTO convertTaskToDTO(Task task) {

        return modelMapper.map(task, TaskDTO.class);
    }

}
