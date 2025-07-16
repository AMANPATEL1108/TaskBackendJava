package com.example.Task.api.DTO;

public class MoveTaskRequest {

    private Long newMenuId;  // Field to store the new menu ID

    public Long getNewMenuId() {
        return newMenuId;
    }

    public void setNewMenuId(Long newMenuId) {
        this.newMenuId = newMenuId;
    }

    @Override
    public String toString() {
        return "MoveTaskRequest{" +
                "newMenuId=" + newMenuId +
                '}';
    }
}
