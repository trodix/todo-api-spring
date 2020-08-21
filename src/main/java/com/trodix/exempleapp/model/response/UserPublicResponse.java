package com.trodix.exempleapp.model.response;

import java.util.UUID;

import lombok.Data;

@Data
public class UserPublicResponse {
    
    UUID id;
    String username;

}