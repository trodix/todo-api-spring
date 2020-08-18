package com.trodix.exempleapp.mapping;

import com.trodix.exempleapp.entity.Todo;
import com.trodix.exempleapp.model.request.TodoRequest;
import com.trodix.exempleapp.model.response.TodoResponse;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    // out
    TodoResponse entityToModel(Todo todo);
    Iterable<TodoResponse> entityListToModel(Iterable<Todo> todos);

    // in
    Todo modelToEntity(TodoRequest todoRequest);

}