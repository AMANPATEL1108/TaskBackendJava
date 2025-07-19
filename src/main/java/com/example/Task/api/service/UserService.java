package com.example.Task.api.service;

import com.example.Task.api.DTO.UserDTO;
import com.example.Task.api.model.User;
import com.example.Task.api.repositoey.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        updateAllUsersAge();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;


    public void createUser(UserDTO userDTO) {
        User user = new User();

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setAddress(userDTO.getAddress());
        user.setCity(userDTO.getCity());
        user.setState(userDTO.getState());
        user.setCountry(userDTO.getCountry());
        user.setImageUrl(userDTO.getImageUrl());

        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));  // ✅ bcrypt hashed
        user.setDateofbirth(userDTO.getDateofbirth());

        // Calculate age from DOB
        if (userDTO.getDateofbirth() != null) {
            LocalDate dob = convertToLocalDate(userDTO.getDateofbirth());
            int age = Period.between(dob, LocalDate.now()).getYears();
            user.setAge(age);
        }

        // Default role
        if (userDTO.getRole() == null || userDTO.getRole().isBlank()) {
            user.setRole("USER");
        } else {
            user.setRole(userDTO.getRole());
        }

        userRepository.save(user);
    }
    public UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        dto.setState(user.getState());
        dto.setCountry(user.getCountry());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setAge(user.getAge());
        dto.setRole(user.getRole());
        dto.setImageUrl(user.getImageUrl());
        dto.setDateofbirth(user.getDateofbirth());
        return dto;
    }


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User save(User user) {
        if (user.getDateofbirth() != null) {
            LocalDate dob = convertToLocalDate(user.getDateofbirth());
            int age = Period.between(dob, LocalDate.now()).getYears();
            user.setAge(age);
        }
        return userRepository.save(user);
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public List<User> getUsersSortedByUpcomingBirthdays() {
        List<User> users = userRepository.findAll().stream()
                .filter(user -> user.getDateofbirth() != null)
                .collect(Collectors.toList());

        LocalDate today = LocalDate.now();

        return users.stream()
                .map(user -> {
                    LocalDate dob = convertToLocalDate(user.getDateofbirth());
                    LocalDate nextBirthday = dob.withYear(today.getYear());

                    if (nextBirthday.isBefore(today) || nextBirthday.equals(today)) {
                        nextBirthday = nextBirthday.plusYears(1);
                    }

                    return new AbstractMap.SimpleEntry<>(nextBirthday, user);
                })
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private LocalDate convertToLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public void updateAllUsersAge() {
        List<User> users = userRepository.findAll();
        LocalDate today = LocalDate.now();

        for (User user : users) {
            if (user.getDateofbirth() != null) {
                LocalDate dob = convertToLocalDate(user.getDateofbirth());
                int age = Period.between(dob, today).getYears();
                user.setAge(age);
            }
        }

        userRepository.saveAll(users);
    }

    public String updateById(Long id, User user, MultipartFile file) {
            Optional<User> optionalUser = userRepository.findById(id);

            if (optionalUser.isPresent()) {
                User existingUser = optionalUser.get();

                if (user.getFirstName() != null) existingUser.setFirstName(user.getFirstName());
                if (user.getLastName() != null) existingUser.setLastName(user.getLastName());
                if (user.getEmail() != null) existingUser.setEmail(user.getEmail());
                if (user.getAddress() != null) existingUser.setAddress(user.getAddress());
                if (user.getCity() != null) existingUser.setCity(user.getCity());
                if (user.getState() != null) existingUser.setState(user.getState());
                if (user.getCountry() != null) existingUser.setCountry(user.getCountry());
                if (user.getUsername() != null) existingUser.setUsername(user.getUsername());

                if (user.getPassword() != null) {
                    String encodedPassword = passwordEncoder.encode(user.getPassword());
                    existingUser.setPassword(encodedPassword);
                }

                if (user.getAge() != null) existingUser.setAge(user.getAge());
                if (user.getRole() != null) existingUser.setRole(user.getRole());
                if (user.getDateofbirth() != null) existingUser.setDateofbirth(user.getDateofbirth());
                if (user.getImageUrl() != null) existingUser.setImageUrl(user.getImageUrl());

                userRepository.save(existingUser);
                return "User details successfully updated.";
            } else {
                return "User not found with ID: " + id;
            }
        }

}
