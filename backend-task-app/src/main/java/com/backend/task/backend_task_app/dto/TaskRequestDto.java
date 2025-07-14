package com.backend.task.backend_task_app.dto;

import com.backend.task.backend_task_app.model.Task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class TaskRequestDto {
	private Long id;
	private String taskName;
	private int duration;
	private boolean completed;
	private boolean confirmed;
	private Long assignedToId;
	private String assignedTo;

	// Constructor
	public TaskRequestDto(Task task) {
		this.id = task.getId();
		this.taskName = task.getTaskName();
		this.duration = task.getDuration();
		this.completed = task.isCompleted();
		this.confirmed = task.isConfirmed();
		this.assignedToId = task.getAssignedTo() != null ? task.getId() : null;
		this.assignedTo = task.getAssignedTo() != null ? task.getAssignedTo() : "Unassigned";
	}

	// Getters and Setters

}
