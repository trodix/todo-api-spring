package com.trodix.exempleapp.service;

import java.util.Optional;
import java.util.UUID;

import com.trodix.exempleapp.entity.Todo;
import com.trodix.exempleapp.repository.TodoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public Optional<Todo> getOne(UUID id) {
        return this.todoRepository.findById(id);
    }
    
}