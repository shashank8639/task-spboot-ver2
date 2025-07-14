package com.backend.task.backend_task_app.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.task.backend_task_app.dto.UserRequest;
import com.backend.task.backend_task_app.model.Role;
import com.backend.task.backend_task_app.model.User;
import com.backend.task.backend_task_app.repository.RoleRepository;
import com.backend.task.backend_task_app.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
	private BCryptPasswordEncoder passwordEncoder;


    public User getUserByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsernameWithRolesNative(username);
        return userOpt.orElse(null);
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsernameWithRolesNative(username).isPresent();
    }
    
    public User registerUser(UserRequest userRequest) {
    	
    	boolean isFirstUser = userRepository.count() == 0;

    	if (usernameExists(userRequest.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        if (isFirstUser) {
            // First user gets ROLE_ADMIN
            Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

            user.setRoles(Collections.singleton(adminRole));
            user.setAdmin(true);
        }

        userRepository.save(user);
        return user;
    }


	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	 
	public void assignRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);
        
        userRepository.save(user);
    }

	@Transactional
	public void assignRoleToUser(String username, String roleName) {
	    User user = userRepository.findByUsernameWithRolesNative(username)
	        .orElseThrow(() -> new RuntimeException("User not found"));

	    Role role = roleRepository.findByRoleName(roleName)
	        .orElseThrow(() -> new RuntimeException("Role not found"));

	    if (!user.getRoles().contains(role)) {
	        user.getRoles().add(role);
	    }

	    if ("ROLE_ADMIN".equals(role.getRoleName())) {
	        user.setAdmin(true);
	    }

	    userRepository.save(user); // JPA handles the rest
	}


}


