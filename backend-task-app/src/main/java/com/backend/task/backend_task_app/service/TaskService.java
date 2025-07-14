package com.backend.task.backend_task_app.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.backend.task.backend_task_app.dto.TaskRequestDto;
import com.backend.task.backend_task_app.dto.TaskUpdateDTO;
import com.backend.task.backend_task_app.model.Task;
import com.backend.task.backend_task_app.model.User;
import com.backend.task.backend_task_app.repository.TaskRepository;
import com.backend.task.backend_task_app.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    // Create a task (Admin)
    public Task createTask(TaskRequestDto request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String adminUsername = auth.getName();

        User admin = userRepository.findByUsernameWithRolesNative(adminUsername)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Task task = new Task();
        task.setTaskName(request.getTaskName());
        task.setDuration(request.getDuration());
        task.setDuration(request.getDuration());
        task.setAssignedTo(request.getAssignedTo());
        task.setCreatedBy(admin);

        return taskRepository.save(task);
    }

    // Get all tasks (Admin)
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Get tasks for normal user
    public List<Task> getUserTasks(String username) {
    	
    	List<Task> tasks = taskRepository.findByAssignedTo(username);
    	
        return tasks.stream()
        		.filter(task -> task.getAssignedTo().equals(username))
        		.collect(Collectors.toList());
    }

    // Get tasks pending confirmation (Admin)
    public List<Task> getPendingTasks() {
        return taskRepository.findByIsCompletedTrueAndIsConfirmedFalse();
    }

    // Update task completion status
    public Task updateTask( Task task) {

        return taskRepository.save(task);
    }

	public Optional<Task> getTaskById(Long id) {

		 return taskRepository.findById(id);
	}

	public boolean deleteTaskById(Long id, String currentUser, boolean isAdmin) {
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();

            // Admin can delete any task, User can only delete their own
            if (isAdmin || task.getAssignedTo().equals(currentUser)) {
                taskRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }

	public List<Task> getCompletedTasks() {
		return taskRepository.findByIsCompletedTrueAndIsConfirmedTrue();
	}

	public List<Task> getTasksByAssignedTo(String username) {

		return this.getUserTasks(username);
	}
}


