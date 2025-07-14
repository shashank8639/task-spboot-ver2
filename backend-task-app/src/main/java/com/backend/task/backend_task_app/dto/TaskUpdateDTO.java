package com.backend.task.backend_task_app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskUpdateDTO {
    private boolean isCompleted;   // User marks the task as completed
    private boolean isConfirmed;   // Admin confirms the task completion
}
