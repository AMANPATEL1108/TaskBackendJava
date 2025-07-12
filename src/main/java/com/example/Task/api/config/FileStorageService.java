package com.example.Task.api.config;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    public String storeFile(MultipartFile file) {
        // Save file to disk and return URL (e.g., /uploads/filename.png)
        // For example:
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get("C:/Users/amanp/OneDrive/Desktop/02/Angular/TODO-List Full Stack/Task/uploads", filename);
        try {
            Files.copy(file.getInputStream(), path);
            return "/uploads/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}

