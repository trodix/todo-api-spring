package com.trodix.exempleapp.repository;

import java.util.Optional;

import com.trodix.exempleapp.entity.ERole;
import com.trodix.exempleapp.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
