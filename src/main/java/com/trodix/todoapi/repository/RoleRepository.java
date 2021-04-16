package com.trodix.todoapi.repository;

import java.util.Optional;

import com.trodix.todoapi.entity.ERole;
import com.trodix.todoapi.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
