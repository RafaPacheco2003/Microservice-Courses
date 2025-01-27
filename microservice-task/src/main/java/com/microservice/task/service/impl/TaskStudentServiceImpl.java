package com.microservice.task.service.impl;

import com.microservice.task.DTO.TaskSubmissionDTO;
import com.microservice.task.DTO.TaskWithSubmissionsDTO;
import com.microservice.task.entity.Task;
import com.microservice.task.entity.TaskSubmission;
import com.microservice.task.http.request.student.TaskSubmissionRequest;
import com.microservice.task.persistence.TaskRepository;
import com.microservice.task.persistence.TaskSubmissionRepository;
import com.microservice.task.service.*;
import com.microservice.task.util.mappers.TaskSubmissionMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class TaskStudentServiceImpl implements TaskStudentService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskSubmissionRepository taskSubmissionRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TaskSubmissionMapper taskSubmissionMapper;

    @Autowired
    private TaskValidatorService taskValidator;

    @Autowired
    private CourseService courseService;

    @Value("${storage.location}")  // Leemos la ubicación configurada en application.yml
    private String storageLocation;

    // Entrega la tarea
    @Override
    public TaskSubmissionDTO submitTask(Long studentId, Long taskId, TaskSubmissionRequest submissionRequest) {
        Task task = taskValidator.validateTaskExists(taskId);
        taskValidator.verifyStudentEnrolledInCourse(studentId, task, studentService);

        // Obtener el archivo PDF desde la solicitud
        MultipartFile pdfFile = submissionRequest.getPdfFile();

        // Guardar el archivo en el servidor
        String storedFilePath = saveFile(pdfFile);

        TaskSubmission taskSubmission = createTaskSubmission(studentId, submissionRequest, task, storedFilePath);
        TaskSubmission savedSubmission = taskSubmissionRepository.save(taskSubmission);

        return taskSubmissionMapper.convertToDTO(savedSubmission);
    }

    // Obtengo una tarea con sus entregas
    @Override
    public TaskWithSubmissionsDTO getTaskWithSubmissions(Long taskId) {
        Task task = taskValidator.validateTaskExists(taskId);
        List<TaskSubmission> taskSubmissions = taskSubmissionRepository.findByTaskId(taskId);
        List<TaskSubmissionDTO> taskSubmissionDTOs = taskSubmissionMapper.convertToDTOList(taskSubmissions);

        return TaskWithSubmissionsDTO.builder()
                .taskId(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .type(task.getType())
                .creationDate(task.getCreationDate())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .status(task.getStatus())
                .teacherComment(task.getTeacherComment())
                .teacherId(task.getTeacherId())
                .courseId(task.getCourseId())
                .submissions(taskSubmissionDTOs)
                .build();
    }

    // Guardar el archivo PDF en el servidor
    private String saveFile(MultipartFile file) {
        // Verificar que el archivo no esté vacío
        if (file.isEmpty()) {
            throw new RuntimeException("No se proporcionó un archivo PDF.");
        }

        try {
            // Crear un directorio si no existe
            File dir = new File(storageLocation);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Guardar el archivo en el directorio configurado
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File serverFile = new File(dir, fileName);
            try (FileOutputStream fos = new FileOutputStream(serverFile)) {
                fos.write(file.getBytes());
            }

            return serverFile.getAbsolutePath();  // Retornar la ruta completa del archivo
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo.", e);
        }
    }

    // Crear la entrega de la tarea
    private TaskSubmission createTaskSubmission(Long studentId, TaskSubmissionRequest submissionRequest, Task task, String storedFilePath) {
        TaskSubmission taskSubmission = new TaskSubmission();
        taskSubmission.setStudentId(studentId);
        taskSubmission.setSubmitted(true);
        taskSubmission.setStudentComment(submissionRequest.getStudentComment());
        taskSubmission.setLate(taskValidator.isLate(task.getEndDate(), submissionRequest.getSubmissionDate()));
        taskSubmission.setSubmissionDate(submissionRequest.getSubmissionDate());
        taskSubmission.setPdfFile(storedFilePath);  // Guardar la ruta del archivo en la base de datos

        taskSubmission.setTask(task);
        return taskSubmission;
    }
}
