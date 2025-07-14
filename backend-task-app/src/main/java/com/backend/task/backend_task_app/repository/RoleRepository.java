package com.backend.task.backend_task_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.task.backend_task_app.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByRoleName(String name);

	@Query(value = """
			SELECT r.* FROM role r
			JOIN user_role ur ON r.id = ur.role_id
			JOIN users u ON ur.user_id = u.id
			WHERE u.username = :username
			""", nativeQuery = true)
	List<Role> findRolesByUsernameNative(@Param("username") String username);

}
