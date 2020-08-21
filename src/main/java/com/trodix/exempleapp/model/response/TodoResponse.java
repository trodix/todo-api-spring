package com.trodix.exempleapp.model.response;

import java.util.UUID;

import lombok.Data;

@Data
public class TodoResponse {
    
    private UUID id;
    private String title;
    private UserPublicResponse user;

}