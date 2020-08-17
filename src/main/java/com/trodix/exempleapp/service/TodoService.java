package com.trodix.exempleapp.service;

import java.util.Optional;
import java.util.UUID;

import com.trodix.exempleapp.entity.Todo;
import com.trodix.exempleapp.exception.ResourceNotFoundException;
import com.trodix.exempleapp.repository.TodoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {

    @Autowired
    private final TodoRepository todoRepository;

    public Optional<Todo> getOne(UUID id) {
        return this.todoRepository.findById(id);
    }

    public Iterable<Todo> getAll() {
        return this.todoRepository.findAll();
    }

    public Todo create(Todo todo) {
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