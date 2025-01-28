package com.microservice.task.service.impl;
import com.microservice.task.DTO.*;
import com.microservice.task.client.StudentClient;

import com.microservice.task.entity.Task;
import com.microservice.task.entity.TaskSubmission;
import com.microservice.task.exception.TaskNotFoundException;
import com.microservice.task.http.request.teacher.GradeTaskRequest;
import com.microservice.task.http.request.teacher.TeacherTaskRequest;
import com.microservice.task.persistence.TaskRepository;
import com.microservice.task.persistence.TaskSubmissionRepository;
import com.microservice.task.service.*;
import com.microservice.task.util.assignments.AssignCourseAndTeacherNamesToDTO;
import com.microservice.task.util.mappers.TaskMapper;
import com.microservice.task.util.mappers.TaskSubmissionMapper;
import com.microservice.task.util.mappers.impl.TaskTeacherMapperImpl;
import com.microservice.task.util.validations.TaskValidatorService;
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
    private ModelMapper modelMapper;

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
    private TaskSubmissionMapper taskSubmissionMapper;

    @Autowired
    private TaskSubmissionRepository taskSubmissionRepository;

    @Autowired
    private TaskValidatorService taskValidator;
    @Autowired
    private AssignCourseAndTeacherNamesToDTO assignCourseAndTeacherNamesToDTO;

    @Autowired
    private StudentClient studentClient;
    @Autowired
    private TaskTeacherMapperImpl taskTeacherMapperImpl;

    @Override
    public TaskDTO addTask(TeacherTaskRequest taskRequest) {
        CourseDTO course = courseService.getCourseById(taskRequest.getCourseId());
        TeacherDTO teacher = teacherService.getTeacherById(taskRequest.getTeacherId());

        Task task = taskMapper.convertRequestToTask(taskRequest);
        List<Long> studentIds = studentService.getStudentIdsByCourse(taskRequest.getCourseId());

        task.setStudentIds(studentIds);
        task.setCreationDate(new Date());

        Task savedTask = taskRepository.save(task);
        TaskDTO taskDTO = taskMapper.convertTaskToDTO(savedTask);

        assignCourseAndTeacherNamesToDTO.assignCourseAndTeacherNamesToDTO(taskDTO, course, teacher);

        return taskDTO;
    }

    @Override
    public TaskDTO updateTask(Long id, TeacherTaskRequest taskRequest) {
        Task task = taskValidator.validateTaskExists(id);
        Task updatedTask = taskMapper.prepareUpdatedTask(id, taskRequest);
        List<Long> studentIds = studentService.getStudentIdsByCourse(taskRequest.getCourseId());
        updatedTask.setStudentIds(studentIds);

        Task savedTask = taskRepository.save(updatedTask);
        TaskDTO taskDTO = taskMapper.convertTaskToDTO(savedTask);

        CourseDTO course = courseService.getCourseById(updatedTask.getCourseId());
        TeacherDTO teacher = teacherService.getTeacherById(updatedTask.getTeacherId());

        assignCourseAndTeacherNamesToDTO.assignCourseAndTeacherNamesToDTO(taskDTO, course, teacher);

        return taskDTO;
    }

    @Override
    public TaskSubmissionDTO gradeTaskSubmission(Long submissionId, GradeTaskRequest gradeTaskRequest) {
        TaskSubmission taskSubmission = taskValidator.validateSubmissionExists(submissionId);
        taskSubmission.setGrade(gradeTaskRequest.getGrade());
        taskSubmission.setTeacherComment(gradeTaskRequest.getTeacherComment());
        taskSubmission.setGradeDate(new Date());

        TaskSubmission updatedSubmission = taskSubmissionRepository.save(taskSubmission);
        return taskSubmissionMapper.convertToDTO(updatedSubmission);
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = getTaskByIdWithException(id);
        TeacherDTO teacher = teacherService.getTeacherById(task.getTeacherId());
        CourseDTO course = courseService.getCourseById(task.getCourseId());

        List<Long> studentIds = studentService.getStudentIdsByCourse(task.getCourseId());
        TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);
        taskDTO.setStudentIds(studentIds);

        assignCourseAndTeacherNamesToDTO.assignCourseAndTeacherNamesToDTO(taskDTO, course, teacher);

        return taskDTO;
    }

    //Ver todas las tareas de un student
    @Override
    public List<TaskDTO> getTasksByStudentId(Long studentId) {
        StudentDTO studentDTO = studentService.getStudentById(studentId);
        List<Task> tasks = taskRepository.findAllByStudentIds(studentId);

        return tasks.stream()
                .map(task -> {
                    TaskDTO taskDTO = taskMapper.convertTaskToDTO(task);

                    CourseDTO course = courseService.getCourseById(task.getCourseId());
                    TeacherDTO teacher = teacherService.getTeacherById(task.getTeacherId());

                    assignCourseAndTeacherNamesToDTO.assignCourseAndTeacherNamesToDTO(taskDTO, course, teacher);

                    return taskDTO;
                })
                .collect(Collectors.toList());
    }

    //Ver todas las tareas por de un course
    @Override
    public List<TaskDTO> getTasksByCourse(Long courseId) {
        CourseDTO courseDTO = courseService.getCourseById(courseId);
        List<Task> tasks = taskRepository.findByCourseId(courseId);
        List<Long> studentIds = studentService.getStudentIdsByCourse(courseId);

        return tasks.stream()
                .map(task -> {
                    TaskDTO dto = modelMapper.map(task, TaskDTO.class);
                    dto.setStudentIds(studentIds);

                    TeacherDTO teacher = teacherService.getTeacherById(task.getTeacherId());
                    assignCourseAndTeacherNamesToDTO.assignCourseAndTeacherNamesToDTO(dto, courseDTO, teacher);

                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = (List<Task>) taskRepository.findAll();

        return tasks.stream()
                .map(task -> {
                    List<Long> studentIds = studentService.getStudentIdsByCourse(task.getCourseId());
                    task.setStudentIds(studentIds);

                    TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);
                    taskDTO.setStudentIds(studentIds);

                    CourseDTO course = courseService.getCourseById(task.getCourseId());
                    TeacherDTO teacher = teacherService.getTeacherById(task.getTeacherId());

                    assignCourseAndTeacherNamesToDTO.assignCourseAndTeacherNamesToDTO(taskDTO, course, teacher);

                    return taskDTO;
                })
                .collect(Collectors.toList());
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




}
