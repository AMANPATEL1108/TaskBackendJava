package com.example.Task.api.controller;

import com.example.Task.api.DTO.TaskDTO;
import com.example.Task.api.DTO.TaskMenuDTO;
import com.example.Task.api.model.Task;
import com.example.Task.api.model.TaskMenu;
import com.example.Task.api.service.TaskMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user/taskmenu")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskMenuController {

    @Autowired
    private TaskMenuService taskMenuService;



    @GetMapping("/get-all-taskmenu")
    public ResponseEntity<List<TaskMenuDTO>> getAllTaskMenus() {
        return ResponseEntity.ok(taskMenuService.getAllTaskMenus());
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

}
