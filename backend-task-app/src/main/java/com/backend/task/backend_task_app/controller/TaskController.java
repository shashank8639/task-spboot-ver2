package com.backend.task.backend_task_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.backend.task.backend_task_app.dto.TaskRequestDto;
import com.backend.task.backend_task_app.dto.TaskUpdateDTO;
import com.backend.task.backend_task_app.model.Task;
import com.backend.task.backend_task_app.service.TaskService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Create task (Admin only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Task> createTask(@RequestBody TaskRequestDto request) {
        Task task = taskService.createTask(request);
        return ResponseEntity.ok(task);
    }
    
    @GetMapping("/assigned/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable String username) {
        return ResponseEntity.ok(taskService.getTasksByAssignedTo(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        Optional<Task> task = taskService.getTaskById(id);

        if (task.isPresent()) {
            Task taskData = task.get();

            // Admin can access any task, user can only access their own tasks
            if (isAdmin || taskData.getAssignedTo().equals(currentUser)) {
                return ResponseEntity.ok(taskData);
            } else {
                return ResponseEntity.status(403).body("You are not authorized to access this task.");
            }
        }

        return ResponseEntity.notFound().build();
    }

    // Get all tasks (Admin only)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    //Get tasks for the current user (Normal users)
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Task>> getUserTasks() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Task> tasks = taskService.getUserTasks(username);
        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Task>> getUserTasksWithUsername(@PathVariable String username) {

        List<Task> tasks = taskService.getUserTasks(username);
        return ResponseEntity.ok(tasks);
    }

    // Get pending confirmation tasks (Admin only)
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Task>> getPendingTasks() {
        List<Task> tasks = taskService.getPendingTasks();
        return ResponseEntity.ok(tasks);
    }
    
    // Get completed tasks (Admin only)
    @GetMapping("/completed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Task>> getCompletedTasks() {
        List<Task> tasks = taskService.getCompletedTasks();
        return ResponseEntity.ok(tasks);
    }

    // Update task status (Admin confirms completion)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Task task = taskService.getTaskById(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));

        if (updates.containsKey("completed")) {
            task.setCompleted((Boolean) updates.get("completed"));
        }
        if (updates.containsKey("confirmed")) {
            task.setConfirmed((Boolean) updates.get("confirmed"));
        }

        taskService.updateTask(task);
        return ResponseEntity.ok(task);
    }
    
    @DeleteMapping("/remove/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTaskById(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        boolean isDeleted = taskService.deleteTaskById(id, currentUser, isAdmin);

        if (isDeleted) {
            return ResponseEntity.ok("Task removed successfully.");
        } else {
            return ResponseEntity.status(403).body("You are not authorized to delete this task.");
        }
    }
}
