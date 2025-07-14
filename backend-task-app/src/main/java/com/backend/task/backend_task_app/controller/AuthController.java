package com.backend.task.backend_task_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.task.backend_task_app.dto.JwtRequest;
import com.backend.task.backend_task_app.dto.JwtResponse;
import com.backend.task.backend_task_app.jwt.JwtAuthHelper;
import com.backend.task.backend_task_app.service.AuthService;
import com.backend.task.backend_task_app.service.UserService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtAuthHelper jwtAuthHelper;
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) {
		
		System.out.println("here ==> d");
		return new ResponseEntity<>(authService.login(jwtRequest),HttpStatus.OK);
				
	}
	
	 @PostMapping("/assign-role/{userId}/{roleId}")
	    public ResponseEntity<String> assignRole(
	            @PathVariable Long userId, 
	            @PathVariable Long roleId) {
	        userService.assignRoleToUser(userId, roleId);
	        return ResponseEntity.ok("Role assigned successfully");
	    }
}

