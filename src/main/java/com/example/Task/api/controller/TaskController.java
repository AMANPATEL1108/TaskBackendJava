package com.example.Task.api.controller;

import com.example.Task.api.DTO.MoveTaskRequest;
import com.example.Task.api.DTO.TaskDTO;
import com.example.Task.api.model.Task;
import com.example.Task.api.model.TaskMenu;
import com.example.Task.api.service.TaskMenuService;
import com.example.Task.api.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/user/tasks")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Absolute upload directory path on your PC
    static final String UPLOAD_DIR = "C:/Users/amanp/OneDrive/Desktop/02/Angular/TODO-List Full Stack/Task/uploads/";

    @Autowired
    private TaskMenuService taskMenuService;



    @GetMapping("/get-all-task")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }



    @PutMapping("/move/{taskId}")
    public ResponseEntity<?> moveTask(@PathVariable Long taskId, @RequestBody Map<String, Long> request) {
        Long newMenuId = request.get("newMenuId");
        try {
            taskService.moveTaskToMenu(taskId, newMenuId); // Your service logic
            return ResponseEntity.ok(Collections.singletonMap("message", "Task moved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to move task"));
        }
    }

}
