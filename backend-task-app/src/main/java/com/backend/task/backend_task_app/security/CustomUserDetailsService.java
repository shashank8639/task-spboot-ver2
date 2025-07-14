package com.backend.task.backend_task_app.security;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.task.backend_task_app.model.Role;
import com.backend.task.backend_task_app.model.User;
import com.backend.task.backend_task_app.repository.RoleRepository;
import com.backend.task.backend_task_app.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    HttpSession session;
    
    @Autowired
    HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByUsernameWithRolesNative(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            session.setAttribute("isAdmin", user.isAdmin());
            
            System.out.println("=========> password: "+user.getPassword());
            System.out.println("=======> "+user.getRoles());
            System.out.println("========> "+("sreeram".equals(username)));
            
           return user;
            
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException("Authentication failed", e);
        }
    }
}