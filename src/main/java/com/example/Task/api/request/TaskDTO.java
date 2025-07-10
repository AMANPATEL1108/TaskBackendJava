package com.example.Task.api.request;

import lombok.Data;
import java.util.Date;

@Data
public class TaskDTO {
    private String name;
    private String description;
    private String priority;
    private String imageUrl;
    private Date endDate;
    private Long userId;
    private Long taskMenuId;
}
