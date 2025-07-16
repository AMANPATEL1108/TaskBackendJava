package com.example.Task.api.controller;

import com.example.Task.api.DTO.UserDTO;
import com.example.Task.api.config.FileStorageService;
import com.example.Task.api.model.User;
import com.example.Task.api.repositoey.UserRepository;
import com.example.Task.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createUser(
            @RequestPart("user") UserDTO userDto,
            @RequestPart("file") MultipartFile file) {

        // ✅ Store file and update DTO
        String imageUrl = fileStorageService.storeFile(file);
        userDto.setImageUrl(imageUrl);

        userService.createUser(userDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User created successfully");
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("deleteById/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/get-all-users")
    public  ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }


            @GetMapping("/findById/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            UserDTO dto = userService.convertToDTO(userOpt.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/upcoming-birthdays")
    public ResponseEntity<List<User>> getUpcomingBirthdays() {
        return ResponseEntity.ok(userService.getUsersSortedByUpcomingBirthdays());
    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable Long id, @RequestBody User user) {
        String message = userService.updateById(id, user);
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.ok(response);
    }





}
