package com.backend.task.backend_task_app.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.task.backend_task_app.model.Role;
import com.backend.task.backend_task_app.service.RoleService;
import com.backend.task.backend_task_app.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;
    private final UserService userService;

    public RoleController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @PostMapping("/add") 
    public ResponseEntity<Role> addRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.saveRole(role));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PostMapping("/assign")
    public ResponseEntity<String> assignRoleToUser(@RequestParam String username, @RequestParam String role) {
        userService.assignRoleToUser(username, role);
        return ResponseEntity.ok("Role assigned successfully!");
    }
}

