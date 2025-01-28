package com.microservice.task.util;

import org.springframework.core.io.Resource;
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
        return null;
    }

    @Override
    public void save(List<MultipartFile> files) throws Exception {
        for (MultipartFile file : files) {
            this.save(file);
        }
    }

    @Override
    public Stream<Path> loadAll() throws Exception {
        return Stream.empty();
    }
}
