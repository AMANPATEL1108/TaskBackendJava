package com.example.Task.api.service;

import com.example.Task.api.DTO.TaskDTO;
import com.example.Task.api.model.Task;
import com.example.Task.api.model.TaskMenu;
import com.example.Task.api.model.User;
import com.example.Task.api.repositoey.TaskMenuRepository;
import com.example.Task.api.repositoey.TaskRepository;
import com.example.Task.api.repositoey.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskMenuRepository taskMenuRepository;
    @Autowired
    private TaskMenuService taskMenuService;

        public Task createTask(TaskDTO dto) {
            if (dto.getUserId() == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }
            if (dto.getTaskMenuId() == null) {
                throw new IllegalArgumentException("TaskMenu ID cannot be null");
            }

            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new NoSuchElementException("User not found with id: " + dto.getUserId()));

            TaskMenu menu = taskMenuRepository.findById(dto.getTaskMenuId())
                    .orElseThrow(() -> new NoSuchElementException("TaskMenu not found with id: " + dto.getTaskMenuId()));

            Task task = new Task();
            task.setName(dto.getName());
            task.setDescription(dto.getDescription());
            task.setPriority(dto.getPriority());
            task.setImageUrl(dto.getImageUrl());
            task.setEndDate(dto.getEndDate());
            task.setAssignedUser(user);
            task.setTaskMenu(menu);

            return taskRepository.save(task);
        }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new NoSuchElementException("Task not found with id: " + taskId);
        }
        taskRepository.deleteById(taskId);
    }

    public void moveTaskToMenu(Long taskId, Long newMenuId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        TaskMenu newMenu = taskMenuRepository.findById(newMenuId)
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        task.setTaskMenu(newMenu);

        taskRepository.save(task);
    }

    public Optional<Task> findTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public void save(Optional<Task> task) {
        if (task.isPresent()) {
            taskRepository.save(task.get());
        }
    }
}
