package com.backend.task.backend_task_app.service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.backend.task.backend_task_app.dto.JwtRequest;
import com.backend.task.backend_task_app.dto.JwtResponse;
import com.backend.task.backend_task_app.jwt.JwtAuthHelper;

@Service
public class AuthService {

	@Autowired
	private JwtAuthHelper jwtHelper;
	
	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	 public JwtResponse login(JwtRequest jwtRequest) {
	        // 1. Authenticate
		 System.out.println("here ==> e");
	        this.doAuthenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
	        
	        // 2. Load UserDetails with roles
	        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());
	        System.out.println("===========> "+userDetails);
	        // 3. Generate token with roles
	       
	        System.out.println("here ==> f");
	        String token = jwtHelper.generateToken(userDetails);
	        System.out.println("here ==> g");
	        return new JwtResponse(token);
	    }
	    
	    private void doAuthenticate(String username, String password) {
	        UsernamePasswordAuthenticationToken authentication = 
	            new UsernamePasswordAuthenticationToken(username, password);
	        manager.authenticate(authentication);
	    }
	
	
}

