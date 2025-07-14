package com.backend.task.backend_task_app.exception;

public class TaskNotFoundException extends RuntimeException {

	private String message;
	
	public TaskNotFoundException(String message) {
		super(message);
	}
}
