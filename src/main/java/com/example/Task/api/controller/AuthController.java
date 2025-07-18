package com.example.Task.api.controller;

import com.example.Task.api.DTO.UserDTO;
import com.example.Task.api.config.FileStorageService;
import com.example.Task.api.model.Person;
import com.example.Task.api.model.User;
import com.example.Task.api.repositoey.UserRepository;
import com.example.Task.api.request.AuthRequest;
import com.example.Task.api.response.ApiResponse;
import com.example.Task.api.service.JwtService;
import com.example.Task.api.service.PersonService;
import com.example.Task.api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    @Autowired
    private AuthenticationManager authManager;
    @Autowired private UserRepository repo;
    @Autowired private PasswordEncoder encoder;
    @Autowired private JwtService jwtService;
    @Autowired private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        // Authenticate username/password
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        // Fetch user from DB
        User user = repo.findByUsername(request.username()).orElseThrow();

        // Generate JWT token
        String token = jwtService.generateToken(user);

        // ✅ Return token, role, and userId as expected by frontend
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole());      // e.g., "ROLE_ADMIN" or "ROLE_USER"
        response.put("userId", user.getId());      // used in frontend for profile/image

        return ResponseEntity.ok(response);
    }


    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> createUser(
            @RequestPart("user") String userJson,
            @RequestPart("file") MultipartFile file) {

        try {
            // Convert JSON string to UserDTO manually
            ObjectMapper mapper = new ObjectMapper();
            UserDTO userDto = mapper.readValue(userJson, UserDTO.class);

            String imageUrl = fileStorageService.storeFile(file);
            userDto.setImageUrl(imageUrl);

            userService.createUser(userDto);

            Map<String, String> response = new HashMap<>();
            response.put("message", "User created successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid data: " + e.getMessage()));
        }
    }


    @GetMapping("/test")
    public String test() {
        return "Public access works";
    }


}
