package com.microservice.task.controller;

import com.microservice.task.util.FileServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/task/files")
public class FileController {

    @Autowired
    private FileServiceApi fileServiceApi;

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("files") List<MultipartFile> files) {
        try {
            fileServiceApi.save(files);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } catch (Exception e) {
            e.printStackTrace();  // Esto ayudará a ver el error detallado en la consola
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error: " + e.getMessage());
        }
    }

    // Endpoint para descargar un archivo por nombre
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            // Usamos el método load que ya está buscando por nombre
            Resource resource = fileServiceApi.load(filename);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Archivo no encontrado
        }
    }
    @GetMapping("/getFilePath/{filename}")
    public ResponseEntity<String> getFilePath(@PathVariable String filename) {
        try {
            // Verifica si el archivo existe
            fileServiceApi.load(filename);  // Si no se encuentra, lanzará una excepción

            // Incluye la URL completa, asegurándote de que sea accesible desde el cliente
            String filePath = "http://localhost:6060/files/download/" + filename;  // Cambia el puerto si es necesario
            return ResponseEntity.ok(filePath);  // Retorna la ruta completa
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Archivo no encontrado");
        }
    }

    @GetMapping("/downloadFile/{filename}")
    public ResponseEntity<Resource> getFileDow(@PathVariable String filename) {
        try {
            // Verifica si el archivo existe
            Resource resource = fileServiceApi.load(filename);  // Obtén el recurso

            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // El recurso no existe
            }

            // Configura los encabezados para indicar que es un archivo para descargar
            String contentDisposition = "attachment; filename=\"" + resource.getFilename() + "\"";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .header(HttpHeaders.CONTENT_TYPE, "application/pdf")  // Aquí puedes cambiar el tipo MIME según el archivo
                    .body(resource);  // Envía el archivo como respuesta
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Maneja cualquier excepción
        }
    }


    // Endpoint para descargar todos los archivos
    @GetMapping("/downloadAll")
    public ResponseEntity<List<String>> downloadAllFiles() {
        try {
            // Devuelve una lista de nombres de archivo
            List<String> fileNames = fileServiceApi.loadAll()
                    .map(path -> path.toString())
                    .collect(Collectors.toList());

            if (fileNames.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);  // No hay archivos
            }

            return ResponseEntity.ok(fileNames);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Error
        }
    }
}
