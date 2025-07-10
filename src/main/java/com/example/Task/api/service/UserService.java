package com.example.Task.api.service;

import com.example.Task.api.DTO.UserDTO;
import com.example.Task.api.model.User;
import com.example.Task.api.repositoey.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void createUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword()); // plain text
        user.setRole(userDTO.getRole()); // assign exactly from DTO, e.g. "ADMIN" or "USER"
        userRepository.save(user);
    }


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }


    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }
}
