package com.example.Task.api.controller;

import com.example.Task.api.model.User;
import com.example.Task.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/basic")
@CrossOrigin(origins = "http://localhost:4200")
public class BasicController {

    @Autowired
    private UserService userService;

    @GetMapping("/upcoming-birthdays")
    public ResponseEntity<List<User>> getUpcomingBirthdays() {
        return ResponseEntity.ok(userService.getUsersSortedByUpcomingBirthdays());
    }
}
