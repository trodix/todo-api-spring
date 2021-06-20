package com.trodix.todoapi.controller;

import java.util.UUID;

import com.trodix.todoapi.core.exception.ResourceNotFoundException;
import com.trodix.todoapi.entity.Todo;
import com.trodix.todoapi.mapping.TodoMapper;
import com.trodix.todoapi.model.request.TodoRequest;
import com.trodix.todoapi.model.response.TodoResponse;
import com.trodix.todoapi.service.TodoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private final TodoService todoService;

    @Autowired
    private final TodoMapper todoMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public TodoResponse getOne(@PathVariable("id") UUID id) {
        Todo result = todoService.getOne(id)
            .orElseThrow(() -> new ResourceNotFoundException("Todo item not found. id: " + id));

        TodoResponse response = todoMapper.entityToModel(result);

        return response;
    }

    @GetMapping()
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<TodoResponse> getAll() {
        Iterable<Todo> result = todoService.getAllByUser();
        Iterable<TodoResponse> response = todoMapper.entityListToModel(result);

        return response;
    }

    @PostMapping()
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public TodoResponse create(@RequestBody TodoRequest todoRequest) {
        Todo todo = todoMapper.modelToEntity(todoRequest);
        todo = todoService.create(todo);
        TodoResponse response = todoMapper.entityToModel(todo);

        return response;
    }

    @PutMapping("/{id}")
    @PreAuthorize("@creatorChecker.check(T(com.trodix.todoapi.entity.Todo), #id) or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public TodoResponse update(@PathVariable("id") UUID id, @RequestBody TodoRequest todoRequest) {
        Todo todo = todoMapper.modelToEntity(todoRequest);

        todo = todoService.update(id, todo);

        TodoResponse response = todoMapper.entityToModel(todo);

        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@creatorChecker.check(T(com.trodix.todoapi.entity.Todo), #id) or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") UUID id) {
        Boolean isDeleted = todoService.delete(id);
        if (isDeleted == false) {
            throw new ResourceNotFoundException("Todo item not found. id: " + id);
        }
    }
    
}