package com.trodix.exempleapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import com.trodix.exempleapp.entity.Todo;
import com.trodix.exempleapp.exception.ResourceNotFoundException;
import com.trodix.exempleapp.service.TodoService;


/**
 * TodosController
 */
@RestController
@RequestMapping("todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/{id}")
    public Todo getOne(@PathVariable("id") UUID id) {
        Todo result = todoService.getOne(id)
            .orElseThrow(() -> new ResourceNotFoundException("Todo item not found. id: " + id));

        return result;
    }
    
}