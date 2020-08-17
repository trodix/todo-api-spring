package com.trodix.exempleapp.mapping;

import java.util.List;

import com.trodix.exempleapp.entity.Todo;
import com.trodix.exempleapp.model.TodoResponse;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    // out
    TodoResponse entityToModel(Todo todo);
    List<TodoResponse> entityListToModel(List<Todo> todos);

    // in
    Todo modelToEntity(TodoResponse todoResponse);

}