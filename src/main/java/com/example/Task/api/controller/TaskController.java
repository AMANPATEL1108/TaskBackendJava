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
@RequestMapping("/tasks")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Absolute upload directory path on your PC
    private static final String UPLOAD_DIR = "C:/Users/amanp/OneDrive/Desktop/02/Angular/TODO-List Full Stack/Task/uploads";

    @Autowired
    private TaskMenuService taskMenuService;

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("priority") String priority,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam("userId") Long userId,
            @RequestParam("taskMenuId") Long taskMenuId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            // Ensure directory exists
            File uploadDirFile = new File(UPLOAD_DIR);
            if (!uploadDirFile.exists()) uploadDirFile.mkdirs();

            // Extract file extension safely
            String originalFilename = imageFile.getOriginalFilename();
            String fileExtension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String newFileName = System.currentTimeMillis() + fileExtension;

            File dest = new File(UPLOAD_DIR + newFileName);
            imageFile.transferTo(dest);

            // *** UPDATED HERE: Store relative URL instead of absolute file system path ***
            // This URL can be used by frontend to access image via HTTP request.
            imageUrl = "/uploads/" + newFileName;  // <-- relative path mapped in WebConfig
        }


        TaskDTO dto = new TaskDTO();
        dto.setName(name);
        dto.setDescription(description);
        dto.setPriority(priority);
        dto.setEndDate(endDate);
        dto.setUserId(userId);
        dto.setTaskMenuId(taskMenuId);
        dto.setImageUrl(imageUrl);

        Task createdTask = taskService.createTask(dto);
        return ResponseEntity.ok(createdTask);
    }


    @GetMapping("/get-all-task")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        System.out.println("ID sio s"+id);
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
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
