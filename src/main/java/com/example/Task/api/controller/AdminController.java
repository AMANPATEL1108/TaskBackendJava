package com.example.Task.api.controller;

import com.example.Task.api.DTO.TaskDTO;
import com.example.Task.api.DTO.TaskMenuDTO;
import com.example.Task.api.DTO.UserDTO;
import com.example.Task.api.config.FileStorageService;
import com.example.Task.api.model.*;
import com.example.Task.api.repositoey.UserRepository;
import com.example.Task.api.response.ApiResponse;
import com.example.Task.api.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private PersonService personService;
    @Autowired
    private TaskMenuService taskMenuService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;




    @GetMapping("/get-all-leaves")
    public List<LeaveSection> getAllLeaves() {
        return leaveService.getAllLeaves();
    }


    @PostMapping("/create-task")
    public ResponseEntity<Task> createTask(
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("priority") String priority,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam("userId") Long userId,
            @RequestParam("taskMenuId") Long taskMenuId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) throws IOException {

        String imageUrl = null;

        if (imageFile != null && !imageFile.isEmpty()) {
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs(); // ✅ Ensure directory exists
            }

            String originalFilename = imageFile.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String newFileName = System.currentTimeMillis() + fileExtension;
            File dest = new File(uploadDirFile, newFileName);
            imageFile.transferTo(dest);

            imageUrl = "/uploads/" + newFileName;  // ✅ This works with your WebConfig
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


    @PostMapping("/create-task-menu")
    public ResponseEntity<TaskMenu> createTaskMenu(@RequestBody TaskMenuDTO taskMenuDTO) {
        TaskMenu created = taskMenuService.createTaskMenu(taskMenuDTO);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/deletetaskmenu/{id}")
    public ResponseEntity<String> deleteTaskMenuWithTasks(@PathVariable Long id) {
        boolean deleted = taskMenuService.deleteTaskMenuWithTasks(id);
        if (deleted) {
            return ResponseEntity.ok("TaskMenu and its associated Tasks deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
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

    private final String UPLOAD_DIR = "uploads/";

    // Create Person - Store image file and save URL
    @PostMapping(value = "/create-person", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPerson(
            @RequestPart("person") Person personDTO, // The person object that comes as JSON
            @RequestPart(value = "file", required = false) MultipartFile file) { // File part

        try {
            // If a file is uploaded, save it and set the pdfUrl
            if (file != null && !file.isEmpty()) {
                String fileName = saveFile(file); // Save the file and get the filename
                personDTO.setPdfUrl(fileName);    // Save the file URL in the person entity
            }

            // Set timestamps
            personDTO.setCreatedDate(new Date());
            personDTO.setUpdateddate(new Date());

            // Save person to the database
            personService.savePerson(personDTO);

            return ResponseEntity.ok(new ApiResponse(true, "Document created successfully"));
        } catch (Exception e) {
            // Handle errors
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse(false, "Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("delete-task/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        System.out.println("ID sio s"+id);
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-all-persons")
    public ResponseEntity<List<Person>> getAllPersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }

    // Delete Person
    @DeleteMapping("/person-deleteByID/{id}")
    public ResponseEntity<ApiResponse> deletePerson(@PathVariable String id) {
        personService.deletePerson(id);
        return ResponseEntity.ok(new ApiResponse(true, "Person deleted successfully"));
    }


    @PutMapping("/updateById-usserrights/{id}")
    public ResponseEntity<String> updateUserById(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User existingUser = optionalUser.get();
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setRole(updatedUser.getRole());
        existingUser.setDateofbirth(updatedUser.getDateofbirth());

        userRepository.save(existingUser);
        return ResponseEntity.ok("User updated successfully");
    }


    @GetMapping("/get-all-users")
    public  ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
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


    @DeleteMapping("deleteById/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }



    @GetMapping("/get-leave-ById/{id}")
    public Optional<LeaveSection> getLeaveById(@PathVariable Long id){
        return leaveService.getLeaveById(id);
    }

    @DeleteMapping("/leaveDeleteById/{id}")
    public String leaveDeleteById(@PathVariable Long id){
        return leaveService.deleteLeaveById(id);
    }

    @PutMapping("/updateById-leave/{id}")
    public String updateLeave(@PathVariable Long id, @RequestBody LeaveSection leaveSection){
        return leaveService.updateLeaveById(id, leaveSection);
    }

    @PutMapping(value = "updateById-person/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePerson(
            @PathVariable String id,
            @RequestPart("person") Person personDTO, // The person object that comes as JSON
            @RequestPart(value = "file", required = false) MultipartFile file) { // File part

        Optional<Person> opt = personService.getPersonById(id);
        if (!opt.isPresent()) return ResponseEntity.notFound().build();

        Person p = opt.get();

        // If a file is uploaded, save it and set the pdfUrl
        if (file != null && !file.isEmpty()) {
            String fileName = saveFile(file); // Save the file and get the filename
            p.setPdfUrl(fileName);           // Update the file URL
        }

        // Update other fields if provided
        if (personDTO.getDocumentName() != null) p.setDocumentName(personDTO.getDocumentName());
        if (personDTO.getOwnerofDocument() != null) p.setOwnerofDocument(personDTO.getOwnerofDocument());

        p.setUpdateddate(new Date());
        personService.savePerson(p);

        return ResponseEntity.ok(new ApiResponse(true, "Document updated successfully"));
    }

    @Autowired
    private UserRepository userRepository;




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

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> createUser(
            @RequestPart("user") UserDTO userDto, // ✅ use DTO directly
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                String imageUrl = fileStorageService.storeFile(file);
                userDto.setImageUrl(imageUrl);
            }

            userService.createUser(userDto); // ✅ You should convert DTO to Entity inside service

            return ResponseEntity.ok(Map.of("message", "User registered successfully"));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Registration failed", "details", e.getMessage()));
        }
    }




}