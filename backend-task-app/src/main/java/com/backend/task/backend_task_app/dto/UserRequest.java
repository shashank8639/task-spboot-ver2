package com.backend.task.backend_task_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequest {

	String username;
	String password;
}
