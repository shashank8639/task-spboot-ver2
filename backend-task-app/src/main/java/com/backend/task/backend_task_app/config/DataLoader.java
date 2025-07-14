package com.backend.task.backend_task_app.config;

import com.backend.task.backend_task_app.model.Role;
import com.backend.task.backend_task_app.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final RoleRepository roleRepository;

    //During App start of the process Roles are initialized
    @PostConstruct
    public void loadRoles() {
    	//Roles will be created only when there are no Roles in Database
        if (roleRepository.count() == 0) {
        	//roleRepository.count()  gives no. of rows in Role Table
            Role admin = new Role();
            admin.setRoleName("ROLE_ADMIN");

            Role user = new Role();
            user.setRoleName("ROLE_USER");

            roleRepository.save(admin);
            roleRepository.save(user);

            System.out.println("Roles initialized.");
        }
    }
}
