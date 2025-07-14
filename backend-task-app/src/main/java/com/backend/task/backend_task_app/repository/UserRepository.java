package com.backend.task.backend_task_app.repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.task.backend_task_app.model.Role;
import com.backend.task.backend_task_app.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = """
			SELECT u.*, r.id as role_id, r.role_name
			FROM users u
			LEFT JOIN user_role ur ON u.id = ur.user_id
			LEFT JOIN role r ON ur.role_id = r.id
			WHERE u.username = :username
			""", nativeQuery = true)
	List<Object[]> findByUsernameWithRoles(@Param("username") String username);
	
	default Optional<User> findByUsernameWithRolesNative(String username) {
        List<Object[]> results = findByUsernameWithRoles(username);
        
        if (results.isEmpty()) {
            return Optional.empty();
        }

        // Group results by user (same user may have multiple roles)
        Map<Long, User> users = new HashMap<>();
      
        for (Object[] row : results) {
            Long userId = ((Number) row[0]).longValue();
            
            User user = users.computeIfAbsent(userId, id -> {
                User u = new User();
                u.setId(id);
                u.setUsername((String) row[3]);
                u.setPassword((String) row[2]);
                u.setAdmin((boolean) row[1]);
                u.setRoles(new HashSet<>());
                return u;
            });

            // Add role if exists (left join may produce nulls)
            if (row[4] != null) {
                Role role = new Role();
                role.setId(((Number) row[4]).longValue());
                role.setRoleName((String) row[5]);
                user.getRoles().add(role);
            }
        }
        
        return Optional.of(users.values().iterator().next());
    }
	
	@Query("SELECT u FROM User u WHERE :role NOT MEMBER OF u.roles")
    List<User> findUsersWithoutRole(@Param("role") Role role);
}
