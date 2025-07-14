package com.backend.task.backend_task_app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String taskName;
	private int duration;
	//Assigned user
	private String assignedTo;

	// Track if task is marked completed by the user
	@Column(nullable = false)
	private boolean isCompleted;

	// Track if the admin has confirmed the completion
	@Column(nullable = false)
	private boolean isConfirmed;

	@ManyToOne
	@JoinColumn(name = "created_by_id", nullable = false)
	private User createdBy; // Admin who created the task
}
