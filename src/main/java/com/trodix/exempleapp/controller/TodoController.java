package com.trodix.exempleapp.controller;

import java.util.UUID;

import com.trodix.exempleapp.entity.Todo;
import com.trodix.exempleapp.exception.ResourceNotFoundException;
import com.trodix.exempleapp.mapping.TodoMapper;
import com.trodix.exempleapp.model.TodoRequest;
import com.trodix.exempleapp.model.TodoResponse;
import com.trodix.exempleapp.service.TodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;


/**
 * TodosController
 */
@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {

    @Autowired
    private final TodoService todoService;

    @Autowired
    private final TodoMapper todoMapper;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TodoResponse getOne(@PathVariable("id") UUID id) {
        Todo result = todoService.getOne(id)
            .orElseThrow(() -> new ResourceNotFoundException("Todo item not found. id: " + id));

        TodoResponse response = todoMapper.entityToModel(result);

        return response;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Iterable<TodoResponse> getAll() {
        Iterable<Todo> result = todoService.getAll();

        Iterable<TodoResponse> response = todoMapper.entityListToModel(result);

        return response;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public TodoResponse create(@RequestBody TodoRequest todoRequest) {
        Todo todo = todoMapper.modelToEntity(todoRequest);

        todo = todoService.create(todo);

        TodoResponse response = todoMapper.entityToModel(todo);

        return response;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TodoResponse update(@PathVariable("id") UUID id, @RequestBody TodoRequest todoRequest) {
        Todo todo = todoMapper.modelToEntity(todoRequest);

        todo = todoService.update(id, todo);

        TodoResponse response = todoMapper.entityToModel(todo);

        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") UUID id) {
        Boolean isDeleted = todoService.delete(id);
        if (isDeleted == false) {
            throw new ResourceNotFoundException("Todo item not found. id: " + id);
        }
    }
    
}