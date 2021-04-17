package com.trodix.todoapi.mapping;

import com.trodix.todoapi.entity.User;
import com.trodix.todoapi.model.request.UserPublicRequest;
import com.trodix.todoapi.model.response.UserPublicResponse;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    // out
    UserPublicResponse entityToModel(User user);
    Iterable<UserPublicResponse> entityListToModel(Iterable<User> users);

    // in
    User modelToEntity(UserPublicRequest userRequest);

}