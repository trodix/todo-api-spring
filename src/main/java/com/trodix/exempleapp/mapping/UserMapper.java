package com.trodix.exempleapp.mapping;

import com.trodix.exempleapp.entity.User;
import com.trodix.exempleapp.model.request.UserPublicRequest;
import com.trodix.exempleapp.model.response.UserPublicResponse;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    // out
    UserPublicResponse entityToModel(User user);
    Iterable<UserPublicResponse> entityListToModel(Iterable<User> users);

    // in
    User modelToEntity(UserPublicRequest userRequest);

}