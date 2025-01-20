package com.microservice.task.service.impl;
import com.microservice.task.DTO.CourseDTO;
import com.microservice.task.DTO.StudentDTO;
import com.microservice.task.DTO.TaskDTO;
import com.microservice.task.DTO.TeacherDTO;
import com.microservice.task.client.StudentClient;

import com.microservice.task.entity.Task;
import com.microservice.task.entity.TaskStatus;
import com.microservice.task.exception.TaskNotFoundException;
import com.microservice.task.http.request.teacher.TeacherTaskGradeRequest;
import com.microservice.task.http.request.teacher.TeacherTaskRequest;
import com.microservice.task.persistence.TaskRepository;
import com.microservice.task.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskTeacherServiceImpl implements TaskTeacherService {
    @Autowired
    private  ModelMapper modelMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TaskMapper taskMapper;


    @Autowired
    private StudentClient studentClient;



    @Override
    public TaskDTO addTask(TeacherTaskRequest taskRequest) {

        CourseDTO course = courseService.getCourseById(taskRequest.getCourseId());
        TeacherDTO teacher = teacherService.getTeacherById(taskRequest.getTeacherId());


        Task task = taskMapper.convertRequestToTask(taskRequest);


        List<Long> studentIds = studentService.getStudentIdsByCourse(taskRequest.getCourseId());

        task.setStudentIds(studentIds);
        task.setCreationDate(new Date());

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
        Task updatedTask = taskMapper.prepareUpdatedTask(id, taskRequest);

        // Obtener la lista de estudiantes y asignarles IDs
        List<Long> studentIds = studentService.getStudentIdsByCourse(taskRequest.getCourseId());

        // Asignar los IDs de estudiantes a la tarea (solo lógicamente)
        updatedTask.setStudentIds(studentIds);

        // Guardar la tarea actualizada en la base de datos
        Task savedTask = taskRepository.save(updatedTask);

        // Convertir la tarea guardada a DTO y devolverlo
        return convertTaskToDTO(savedTask);
    }

    @Override
    public TaskDTO gradeTask(Long id, TeacherTaskGradeRequest gradeRequest) {

        Task task = getTaskByIdWithException(id);


        // Mapear el gradeRequest a la tarea existente usando ModelMapper
        modelMapper.map(gradeRequest, task);

        if (task.getGrade() >= 70) {
            task.setStatus(TaskStatus.GRADED);
        } else {
            task.setStatus(TaskStatus.RETURNED); // Detiene la ejecución si el grade es menor a 70
        }
        task.setUpdatedDate(new Date()); // Actualizar la fecha de la última modificación

        // Guardar la tarea actualizada en la base de datos
        Task savedTask = taskRepository.save(task);

        // Convertir la tarea guardada a DTO y retornarla
        return convertTaskToDTO(savedTask);
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = getTaskByIdWithException(id);

        TeacherDTO teacher = teacherService.getTeacherById(task.getTeacherId());
        CourseDTO course = courseService.getCourseById(task.getCourseId());


        List<Long> studentIds =studentService.getStudentIdsByCourse(task.getCourseId());

        // Convertir la tarea a DTO y asignar los IDs de estudiantes
        TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);
        taskDTO.setStudentIds(studentIds);

        // Asignar el nombre del curso y del profesor
        assignCourseAndTeacherNamesToDTO(taskDTO, course, teacher);

        return taskDTO;
    }
    public List<TaskDTO> getTasksByStudentId(Long studentId) {

        StudentDTO studentDTO = studentService.getStudentById(studentId);

        List<Task> tasks = taskRepository.findAllByStudentIds(studentId);

        // Conversión de Task a TaskDTO usando ModelMapper
        return tasks.stream()
                .map(this::convertTaskToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<TaskDTO> getTasksByCourse(Long courseId) {
        // Verificar si el curso existe usando el CourseClient
        CourseDTO courseDTO = courseService.getCourseById(courseId);

        List<Task> tasks = taskRepository.findByCourseId(courseId);

        List<Long> studentIds = studentService.getStudentIdsByCourse(courseId);

        // Convertir la lista de tareas a una lista de TaskDTO usando ModelMapper
        return tasks.stream()
                .map(task -> {
                    TaskDTO dto = modelMapper.map(task, TaskDTO.class);
                    dto.setStudentIds(studentIds);  // Asignar los IDs de estudiantes
                    return dto;
                })
                .collect(Collectors.toList());
    }
    @Override
    public List<TaskDTO> getAllTasks() {
        // Obtener todas las tareas desde el repositorio
        List<Task> tasks = (List<Task>) taskRepository.findAll();

        // Convertir la lista de tareas a una lista de TaskDTO
        return tasks.stream()
                .map(task -> {

                    List<Long> studentIds = studentService.getStudentIdsByCourse(task.getCourseId());

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
    public void deleteTask(Long id) {
        taskRepository.findById(id)
                .ifPresentOrElse(taskRepository::delete,
                        () -> { throw new TaskNotFoundException(id); });
    }













    private Task getTaskByIdWithException(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
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


    // Conversión de Task a TaskDTO
    private TaskDTO convertTaskToDTO(Task task) {

        return modelMapper.map(task, TaskDTO.class);
    }

}
