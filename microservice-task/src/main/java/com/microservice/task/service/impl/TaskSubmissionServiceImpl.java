package com.microservice.task.service.impl;

import com.microservice.task.DTO.StudentDTO;
import com.microservice.task.DTO.TaskSubmissionDTO;
import com.microservice.task.DTO.TaskWithSubmissionsDTO;
import com.microservice.task.DTO.TeacherDTO;
import com.microservice.task.entity.Task;
import com.microservice.task.entity.TaskSubmission;
import com.microservice.task.http.request.student.TaskSubmissionRequest;
import com.microservice.task.http.request.teacher.GradeTaskRequest;
import com.microservice.task.persistence.TaskSubmissionRepository;
import com.microservice.task.service.*;
import com.microservice.task.util.FileServiceApi;
import com.microservice.task.util.mappers.TaskSubmissionMapper;
import com.microservice.task.util.validations.TaskValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
public class TaskSubmissionServiceImpl implements TaskSubmissionService {

    private final FileServiceApi fileServiceApi;
    private final TaskSubmissionRepository taskSubmissionRepository;
    private final StudentService studentService;
    private final TaskSubmissionMapper taskSubmissionMapper;
    private final TaskValidatorService taskValidator;
    private final TeacherService teacherService;

    @Autowired
    public TaskSubmissionServiceImpl(
            FileServiceApi fileServiceApi,
            TaskSubmissionRepository taskSubmissionRepository,
            StudentService studentService,
            TaskSubmissionMapper taskSubmissionMapper,
            TaskValidatorService taskValidator,
            TeacherService teacherService
    ) {
        this.fileServiceApi = fileServiceApi;
        this.taskSubmissionRepository = taskSubmissionRepository;
        this.studentService = studentService;
        this.taskSubmissionMapper = taskSubmissionMapper;
        this.taskValidator = taskValidator;
        this.teacherService = teacherService;
    }


    @Override
    public TaskSubmissionDTO submitTaskOnce(Long studentId, Long taskId, TaskSubmissionRequest submissionRequest) {
        // Verificamos si existe el task e igual verificamos si el usuario está inscrito en ese curso
        Task task = taskValidator.validateTaskExists(taskId);
        taskValidator.verifyStudentEnrolledInCourse(studentId, task, studentService);

        // Verificamos si el estudiante ya ha entregado la tarea
        TaskSubmission existingSubmission = taskSubmissionRepository.findByTaskIdAndStudentId(taskId, studentId);
        if (existingSubmission != null) {
            // Si ya existe una entrega, lanzamos una excepción
            throw new RuntimeException("You have already submitted this task");
        }

        // Obtener el archivo PDF desde la solicitud
        MultipartFile pdfFile = submissionRequest.getPdfFile();
        // Guardar el archivo en el servidor y obtener la URL completa
        String storedFilePath = saveFile(pdfFile);

        // Crear la entidad de entrega de la tarea
        TaskSubmission taskSubmission = taskSubmissionMapper.convertTaskSubmissionRequestToEntity(studentId, submissionRequest, task, storedFilePath);

        taskSubmission.setSubmissionDate(new Date());
        taskSubmission.setSubmitted(true);
        // Guardar la entrega de la tarea en la base de datos
        TaskSubmission savedSubmission = taskSubmissionRepository.save(taskSubmission);

        // Convertir a DTO y agregar la URL del archivo
        TaskSubmissionDTO taskSubmissionDTO = taskSubmissionMapper.convertTaskSubmissionToTaskSubmissionDTO(savedSubmission);

        return taskSubmissionDTO;
    }


    // Entrega la tarea
    @Override
    public TaskSubmissionDTO submitTask(Long studentId, Long taskId, TaskSubmissionRequest submissionRequest) {

        //Verificamos si existe el task e igual verificamos si el usuario esta en ese course
        Task task = taskValidator.validateTaskExists(taskId);
        taskValidator.verifyStudentEnrolledInCourse(studentId, task, studentService);

        // Obtener el archivo PDF desde la solicitud
        MultipartFile pdfFile = submissionRequest.getPdfFile();
        // Guardar el archivo en el servidor y obtener la URL completa
        String storedFilePath = saveFile(pdfFile);

        // Crear la entidad de entrega de la tarea
        TaskSubmission taskSubmission = taskSubmissionMapper.convertTaskSubmissionRequestToEntity(studentId, submissionRequest, task, storedFilePath);

        taskSubmission.setSubmissionDate(new Date());
        taskSubmission.setSubmitted(true);
        // Guardar la entrega de la tarea en la base de datos
        TaskSubmission savedSubmission = taskSubmissionRepository.save(taskSubmission);

        // Convertir a DTO y agregar la URL del archivo
        TaskSubmissionDTO taskSubmissionDTO = taskSubmissionMapper.convertTaskSubmissionToTaskSubmissionDTO(savedSubmission);

        return taskSubmissionDTO;
    }


    @Override
    public TaskSubmissionDTO gradeTaskSubmission(Long teacherId, Long submissionId, GradeTaskRequest gradeRequest) {
        // Validar si la entrega existe
        TaskSubmission taskSubmission = taskSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Task submission not found"));

        // Obtener la tarea asociada a la entrega
        Task task = taskSubmission.getTask();

        TeacherDTO teacher = teacherService.getTeacherById(task.getTeacherId());

        // Verificar que el profesor que califica la entrega sea el profesor de la tarea
        if (!teacher.getId().equals(teacherId)) {
            throw new RuntimeException("You are not authorized to grade this submission");
        }

        // Actualizar la calificación y el comentario del profesor
        taskSubmission.setGrade(gradeRequest.getGrade());
        taskSubmission.setTeacherComment(gradeRequest.getTeacherComment());
        taskSubmission.setGradeDate(new Date());  // Asignar la fecha actual como la fecha de calificación

        // Guardar los cambios en la base de datos
        TaskSubmission updatedSubmission = taskSubmissionRepository.save(taskSubmission);

        // Convertir la entrega actualizada a DTO
        return taskSubmissionMapper.convertTaskSubmissionToTaskSubmissionDTO(updatedSubmission);
    }



    // Obtengo una tarea con sus entregas de los student
    @Override
    public TaskWithSubmissionsDTO getTaskWithSubmissions(Long taskId) {
        Task task = taskValidator.validateTaskExists(taskId);
        List<TaskSubmission> taskSubmissions = taskSubmissionRepository.findByTaskId(taskId);
        List<TaskSubmissionDTO> taskSubmissionDTOs = taskSubmissionMapper.convertListTaskSubmissionToListTaskSubmissionDTO(taskSubmissions);

        return TaskWithSubmissionsDTO.builder()
                .taskId(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .type(task.getType())
                .creationDate(task.getCreationDate())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .teacherId(task.getTeacherId())
                .courseId(task.getCourseId())
                .submissions(taskSubmissionDTOs)
                .build();
    }
    @Override
    public TaskWithSubmissionsDTO getTaskWithSubmissionsForStudent(Long studentId, Long taskId) {
        //Obtengo task
        Task task = taskValidator.validateTaskExists(taskId);
        //Obtengo studentDTO
        StudentDTO studentDTO = studentService.getStudentById(studentId);

        List<TaskSubmission> taskSubmissions = taskSubmissionRepository.findByTaskId(taskId);
        // Filtrar las entregas por el studentId
        List<TaskSubmission> filteredSubmissions = taskSubmissions.stream()
                .filter(submission -> submission.getStudentId().equals(studentId))
                .toList();
        // Convertir las entregas filtradas a DTOs
        List<TaskSubmissionDTO> taskSubmissionDTOs = taskSubmissionMapper.convertListTaskSubmissionToListTaskSubmissionDTO(filteredSubmissions);

        // Construir y devolver el objeto con la tarea y las entregas filtradas
        return TaskWithSubmissionsDTO.builder()
                .taskId(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .type(task.getType())
                .creationDate(task.getCreationDate())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .teacherId(task.getTeacherId())
                .courseId(task.getCourseId())
                .submissions(taskSubmissionDTOs)
                .build();
    }






















    private String saveFile(MultipartFile file) {
        try {
            // Llama al servicio de archivos para guardar el archivo
            fileServiceApi.save(file);

            // Aquí, construimos la URL completa para la descarga del archivo
            return "http://localhost:6060/files/downloadFile/" + file.getOriginalFilename();  // Usamos el nombre del archivo para construir la URL
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }





}
