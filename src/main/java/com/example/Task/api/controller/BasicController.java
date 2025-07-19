package com.example.Task.api.controller;

import com.example.Task.api.DTO.TaskDTO;
import com.example.Task.api.DTO.TaskMenuDTO;
import com.example.Task.api.DTO.UserDTO;
import com.example.Task.api.model.*;
import com.example.Task.api.service.LeaveService;
import com.example.Task.api.service.PersonService;
import com.example.Task.api.service.TaskMenuService;
import com.example.Task.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/basic")
@CrossOrigin(origins = "http://localhost:4200")
public class BasicController {

    @Autowired
    private UserService userService;


    @Autowired
    private TaskMenuService taskMenuService;
    @Autowired
    private PersonService personService;

    @Autowired
    private LeaveService leaveService;


    @GetMapping("/get-all-leaves")
    public List<LeaveSection> getAllLeaves() {
        return leaveService.getAllLeaves();
    }

    @GetMapping("/upcoming-birthdays")
    public ResponseEntity<List<User>> getUpcomingBirthdays() {
        return ResponseEntity.ok(userService.getUsersSortedByUpcomingBirthdays());
    }
    @GetMapping("/get-all-persons")
    public ResponseEntity<List<Person>> getAllPersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }



    @GetMapping("/get-all-users")
    public  ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/get-all-taskmenu")
    public ResponseEntity<List<TaskMenuDTO>> getAllTaskMenus() {
        return ResponseEntity.ok(taskMenuService.getAllTaskMenus());
    }

    @PutMapping("/update-order/{menuId}")
    public ResponseEntity<TaskMenu> updateTaskMenu(@PathVariable Long menuId, @RequestBody List<TaskDTO> tasks) {
        TaskMenu updatedTaskMenu = taskMenuService.updateTaskMenu(menuId, tasks);
        return ResponseEntity.ok(updatedTaskMenu);
    }

    @PutMapping("/move/{taskId}")
    public ResponseEntity<Task> moveTaskToNewList(@PathVariable Long taskId, @RequestBody Map<String, Long> request) {
        Long newMenuId = request.get("newMenuId");
        Task updatedTask = taskMenuService.moveTaskToNewList(taskId, newMenuId);
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isPresent()) {
            UserDTO dto = userService.convertToDTO(userOpt.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


        @PutMapping(value = "/updateById/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<Map<String, String>> updateUser(
                @PathVariable Long id,
                @RequestPart("user") User user,
                @RequestPart(value = "file", required = false) MultipartFile file) {

            String message = userService.updateById(id, user, file);
            Map<String, String> response = new HashMap<>();
            response.put("message", message);
            return ResponseEntity.ok(response);
        }

    private String saveFile(MultipartFile file) {
        // Ensure the file is a PDF
        if (!file.getContentType().equals("application/pdf")) {
            throw new RuntimeException("Only PDF files are allowed!");
        }

        // Clean the file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Absolute path to the upload folder
        String uploadDir = "C:/Users/amanp/OneDrive/Desktop/02/Angular/TODO-List Full Stack/Task/uploads";  // <-- Use an absolute path

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath); // Create the directory if it doesn't exist
            }

            Path targetLocation = uploadPath.resolve(fileName);

            file.transferTo(targetLocation.toFile());

            return targetLocation.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to store the file " + fileName);
        }
    }

    @PutMapping("/updateById-leave/{id}")
    public String updateLeave(@PathVariable Long id, @RequestBody LeaveSection leaveSection){
        return leaveService.updateLeaveById(id, leaveSection);
    }






}
