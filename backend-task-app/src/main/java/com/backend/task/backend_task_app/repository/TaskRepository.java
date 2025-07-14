package com.backend.task.backend_task_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.task.backend_task_app.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

	List<Task> findByAssignedTo(String username);

    // Find all tasks pending confirmation
    List<Task> findByIsCompletedTrueAndIsConfirmedFalse();

 // Find all tasks which are completed
	List<Task> findByIsCompletedTrueAndIsConfirmedTrue();
}
