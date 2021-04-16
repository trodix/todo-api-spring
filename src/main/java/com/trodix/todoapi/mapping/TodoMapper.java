package com.trodix.todoapi.mapping;

import com.trodix.todoapi.entity.Todo;
import com.trodix.todoapi.model.request.TodoRequest;
import com.trodix.todoapi.model.response.TodoResponse;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    // out
    TodoResponse entityToModel(Todo todo);
    Iterable<TodoResponse> entityListToModel(Iterable<Todo> todos);

    // in
    Todo modelToEntity(TodoRequest todoRequest);

}