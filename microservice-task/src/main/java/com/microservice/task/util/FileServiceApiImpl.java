package com.microservice.task.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileServiceApiImpl implements FileServiceApi {

    private final Path rootFolder = Paths.get("uploads");

    public FileServiceApiImpl() throws IOException {
        // Verifica si el directorio existe, si no, lo crea
        if (!Files.exists(rootFolder)) {
            Files.createDirectories(rootFolder);
        }
    }

    @Override
    public void save(MultipartFile file) throws Exception {
        Files.copy(file.getInputStream(), rootFolder.resolve(file.getOriginalFilename()));
    }

    @Override
    public Resource load(String filename) throws Exception {
        Path file = rootFolder.resolve(filename);
        if (Files.exists(file)) {
            return new FileSystemResource(file);
        } else {
            throw new IOException("Archivo no encontrado: " + filename);
        }
    }

    // Nuevo método para cargar todos los archivos
    @Override
    public Stream<Path> loadAll() throws Exception {
        try {
            return Files.walk(rootFolder, 1)  // 1 es para obtener solo el contenido del directorio
                    .filter(path -> !path.equals(rootFolder))  // Filtramos la ruta raíz
                    .map(rootFolder::relativize);  // Relativizamos las rutas
        } catch (IOException e) {
            throw new IOException("No se pudieron cargar los archivos", e);
        }
    }


    @Override
    public void save(List<MultipartFile> files) throws Exception {
        for (MultipartFile file : files) {
            this.save(file);
        }
    }
}
