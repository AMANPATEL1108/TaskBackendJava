package com.example.Task.api.DTO;

import java.util.Date;

public class TaskDTO {
    private Long id;
    private String name;
    private String description;
    private String priority;
    private String imageUrl;
    private Date endDate;
    private Long userId; // ID of the user assigned to the task
    private Long taskMenuId;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }



    public Long getTaskMenuId() {
        return taskMenuId;
    }

    public void setTaskMenuId(Long taskMenuId) {
        this.taskMenuId = taskMenuId;
    }
}
