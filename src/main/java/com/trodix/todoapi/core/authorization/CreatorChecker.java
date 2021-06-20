package com.trodix.todoapi.core.authorization;

import java.text.MessageFormat;
import java.util.UUID;

import com.trodix.todoapi.core.entity.User;
import com.trodix.todoapi.core.exception.ResourceNotFoundException;
import com.trodix.todoapi.core.interfaces.Ownable;
import com.trodix.todoapi.security.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CreatorChecker {

    @Autowired
    ApplicationContext appContext;

    @Autowired
    private final UserDetailsServiceImpl userDetailsService;

    public CreatorChecker(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public <T extends Ownable> boolean check(Class<T> c, UUID id) {
        String[] beanNamesForType = appContext.getBeanNamesForType(ResolvableType.forClassWithGenerics(CrudRepository.class, c, UUID.class));
        CrudRepository<T, UUID> repository = (CrudRepository<T, UUID>) appContext.getBean(beanNamesForType[0]);
        
        T entity = repository.findById(id).orElse(null);
        if (entity != null) {
            return entity.getUser().isEnabled() && entity.getUser().equals(getAuthenticatedUser());
        }
        throw new ResourceNotFoundException(MessageFormat.format("Entity {0} not found for id {1}", c.getName(), id));
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        return userDetailsService.loadUserEntityByUsername(currentPrincipalName);
    }

}
