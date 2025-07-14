package com.example.Task.api.controller;

import com.example.Task.api.config.JwtUtil;
import com.example.Task.api.model.User;
import com.example.Task.api.repositoey.UserRepository;
import com.example.Task.api.request.LoginRequest;
import com.example.Task.api.response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;  // <-- Set in application.properties or application.yml

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Step 1: Verify CAPTCHA
        boolean captchaVerified = verifyCaptcha(loginRequest.getCaptchaToken());
        if (!captchaVerified) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Captcha verification failed.");
        }

        // Step 2: Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Step 3: Generate JWT and get role
        String jwtToken = jwtUtil.generateToken(authentication.getName());
        String role = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("USER");

        Optional<User> user = userRepository.findByUsername(authentication.getName());

        return ResponseEntity.ok(new LoginResponse(jwtToken, role, user.get().getId()));
    }

    // ✅ Google reCAPTCHA verification method
    private boolean verifyCaptcha(String token) {
        String url = "https://www.google.com/recaptcha/api/siteverify";
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("secret", recaptchaSecret);
        body.add("response", token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            Map<String, Object> responseBody = response.getBody();
            return responseBody != null && Boolean.TRUE.equals(responseBody.get("success"));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
