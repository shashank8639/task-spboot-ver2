package com.backend.task.backend_task_app.service;

import org.springframework.stereotype.Service;

import com.backend.task.backend_task_app.model.Role;
import com.backend.task.backend_task_app.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}


