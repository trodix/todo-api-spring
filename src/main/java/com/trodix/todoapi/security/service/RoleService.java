package com.trodix.todoapi.security.service;

import java.util.HashSet;
import java.util.Set;

import com.trodix.todoapi.entity.ERole;
import com.trodix.todoapi.entity.Role;
import com.trodix.todoapi.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public void initDefaultRoles() {
        Set<Role> roleSet = new HashSet<>();

        Role adminRole = new Role(ERole.ROLE_ADMIN);
        Role moderatorRole = new Role(ERole.ROLE_MODERATOR);
        Role userRole = new Role(ERole.ROLE_USER);

        roleSet.add(adminRole);
        roleSet.add(moderatorRole);
        roleSet.add(userRole);

        for (Role role : roleSet) {
            if (!this.roleRepository.findByName(role.getName()).isPresent()) {
                this.roleRepository.save(role);
            }
        }
    }

}
