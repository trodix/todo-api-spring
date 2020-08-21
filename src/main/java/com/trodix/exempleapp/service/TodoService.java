package com.trodix.exempleapp.service;

import java.util.Optional;
import java.util.UUID;

import com.trodix.exempleapp.entity.Todo;
import com.trodix.exempleapp.entity.User;
import com.trodix.exempleapp.exception.ResourceNotFoundException;
import com.trodix.exempleapp.repository.TodoRepository;
import com.trodix.exempleapp.security.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    @Autowired
    private final TodoRepository todoRepository;

    @Autowired
    private final UserDetailsServiceImpl userDetailsService;

    public TodoService(TodoRepository todoRepository, UserDetailsServiceImpl userDetailsService) {
        this.todoRepository = todoRepository;
        this.userDetailsService = userDetailsService;
    }

    public Optional<Todo> getOne(UUID id) {
        return this.todoRepository.findById(id);
    }

    public Iterable<Todo> getAll() {
        return this.todoRepository.findAll();
    }

    public Todo create(Todo todo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userDetailsService.loadUserEntityByUsername(currentPrincipalName);
        todo.setUser(user);

        return this.todoRepository.save(todo);
    }

    public Todo update(UUID id, Todo todo) {
        Todo existingTodo = this.todoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Todo item not found. id: " + id));

        // Update the object fields
        existingTodo.setTitle(todo.getTitle());

        return this.todoRepository.save(existingTodo);
    }

    public Boolean delete(UUID id) {
        if (this.todoRepository.existsById(id)) {
            this.todoRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    
}