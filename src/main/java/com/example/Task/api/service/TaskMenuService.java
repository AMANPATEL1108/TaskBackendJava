package com.example.Task.api.service;

import com.example.Task.api.DTO.TaskDTO;
import com.example.Task.api.DTO.TaskMenuDTO;
import com.example.Task.api.model.Task;
import com.example.Task.api.model.TaskMenu;
import com.example.Task.api.model.User;
import com.example.Task.api.repositoey.TaskMenuRepository;
import com.example.Task.api.repositoey.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskMenuService {

    @Autowired
    private TaskMenuRepository taskMenuRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TaskRepository taskRepository;

    public TaskMenu createTaskMenu(TaskMenuDTO dto) {
        TaskMenu menu = new TaskMenu();
        menu.setName(dto.getName());
        return taskMenuRepository.save(menu);
    }

    public List<TaskMenuDTO> getAllTaskMenus() {
        List<TaskMenu> menus = taskMenuRepository.findAll();
        return menus.stream()
                .map(menu -> {
                    TaskMenuDTO dto = new TaskMenuDTO();
                    dto.setId(menu.getId());
                    dto.setName(menu.getName());

                    List<TaskDTO> taskDTOs = menu.getTasks().stream().map(task -> {
                        TaskDTO taskDTO = new TaskDTO();

                        taskDTO.setId(task.getId()); // ✅ Include ID here
                        taskDTO.setName(task.getName());
                        taskDTO.setDescription(task.getDescription());
                        taskDTO.setPriority(task.getPriority());
                        taskDTO.setImageUrl(task.getImageUrl());
                        taskDTO.setEndDate(task.getEndDate());
                        taskDTO.setUserId(task.getAssignedUser().getId());
                        taskDTO.setTaskMenuId(menu.getId());

                        return taskDTO;
                    }).toList();

                    dto.setTasks(taskDTOs);
                    return dto;
                })
                .collect(Collectors.toList());
    }


    public void deleteTaskMenu(Long id) {
        taskMenuRepository.deleteById(id);
    }

    public TaskMenu updateTaskMenu(Long menuId, List<TaskDTO> taskDTOList) {
        // Find TaskMenu by ID, throw error if not found
        TaskMenu taskMenu = taskMenuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("TaskMenu not found"));

        // Clear existing tasks
        taskMenu.getTasks().clear();

        // Loop through the provided TaskDTO list
        for (TaskDTO taskDTO : taskDTOList) {
            // Create a new Task object for each DTO
            Task task = new Task();
            task.setName(taskDTO.getName());
            task.setDescription(taskDTO.getDescription());
            task.setPriority(taskDTO.getPriority());
            task.setEndDate(taskDTO.getEndDate());  // Assuming endDate in DTO is of type Date

            // Find the user by userId (if User entity exists in the repository)
            User assignedUser = userService.findById(taskDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            task.setAssignedUser(assignedUser); // Set the actual User entity

            task.setTaskMenu(taskMenu); // Associate with the TaskMenu
            taskMenu.getTasks().add(task); // Add the task to TaskMenu's tasks list
        }

        // Save the updated TaskMenu (with new tasks)
        return taskMenuRepository.save(taskMenu);
    }

    public Task moveTaskToNewList(Long taskId, Long newMenuId) {
        // Get the task to update
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Get the new task menu
        TaskMenu newTaskMenu = taskMenuRepository.findById(newMenuId)
                .orElseThrow(() -> new RuntimeException("TaskMenu not found"));

        // Remove task from the current task menu
        task.setTaskMenu(newTaskMenu);

        // Save updated task and task menu
        taskRepository.save(task);
        return task;
    }


    public Optional<TaskMenu> findMenuById(Long newMenuId) {
        return taskMenuRepository.findById(newMenuId);
    }
}
