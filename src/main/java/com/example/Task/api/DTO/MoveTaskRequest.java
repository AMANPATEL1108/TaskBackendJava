package com.example.Task.api.DTO;

public class MoveTaskRequest {

    private Long newMenuId;  // Field to store the new menu ID

    // Getter for newMenuId
    public Long getNewMenuId() {
        return newMenuId;
    }

    // Setter for newMenuId
    public void setNewMenuId(Long newMenuId) {
        this.newMenuId = newMenuId;
    }

    // Optional: Override toString() for debugging or logging
    @Override
    public String toString() {
        return "MoveTaskRequest{" +
                "newMenuId=" + newMenuId +
                '}';
    }
}
