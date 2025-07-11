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

                        taskDTO.setId(task.getId());
                        taskDTO.setName(task.getName());
                        taskDTO.setDescription(task.getDescription());
                        taskDTO.setPriority(task.getPriority());
                        taskDTO.setImageUrl(task.getImageUrl());
                        taskDTO.setEndDate(task.getEndDate());

                        User assignedUser = task.getAssignedUser();
                        if (assignedUser != null) {
                            taskDTO.setUserId(assignedUser.getId());
                        }

                        taskDTO.setTaskMenuId(menu.getId());

                        return taskDTO;
                    }).collect(Collectors.toList());

                    dto.setTasks(taskDTOs);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public boolean deleteTaskMenuWithTasks(Long id) {
        Optional<TaskMenu> taskMenuOptional = taskMenuRepository.findById(id);
        if (taskMenuOptional.isPresent()) {
            try {
                taskMenuRepository.delete(taskMenuOptional.get());
                return true;
            } catch (Exception e) {
                System.out.println("Failed to delete TaskMenu: " + e.getMessage());
                e.printStackTrace(); // See full stack trace in logs
                return false;
            }
        } else {
            return false;
        }
    }


    public TaskMenu updateTaskMenu(Long menuId, List<TaskDTO> taskDTOList) {
        TaskMenu taskMenu = taskMenuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("TaskMenu not found"));

        taskMenu.getTasks().clear();

        for (TaskDTO taskDTO : taskDTOList) {
            Task task = new Task();
            task.setName(taskDTO.getName());
            task.setDescription(taskDTO.getDescription());
            task.setPriority(taskDTO.getPriority());
            task.setEndDate(taskDTO.getEndDate());

            User assignedUser = userService.findById(taskDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            task.setAssignedUser(assignedUser);
            task.setTaskMenu(taskMenu);
            taskMenu.getTasks().add(task);
        }

        return taskMenuRepository.save(taskMenu);
    }

    public Task moveTaskToNewList(Long taskId, Long newMenuId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        TaskMenu newTaskMenu = taskMenuRepository.findById(newMenuId)
                .orElseThrow(() -> new RuntimeException("TaskMenu not found"));

        task.setTaskMenu(newTaskMenu);

        taskRepository.save(task);
        return task;
    }

    public Optional<TaskMenu> findMenuById(Long newMenuId) {
        return taskMenuRepository.findById(newMenuId);
    }
}
