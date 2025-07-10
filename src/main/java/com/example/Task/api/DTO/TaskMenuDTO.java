package com.example.Task.api.DTO;

import java.util.List;

public class TaskMenuDTO {
    private Long id;
    private String name;
    private List<TaskDTO> tasks; // ✅ Include tasks

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }
}
