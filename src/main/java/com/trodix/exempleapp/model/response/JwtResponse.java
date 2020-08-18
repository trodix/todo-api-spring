package com.trodix.exempleapp.model.response;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class JwtResponse {

	private String accessToken;
	private UUID id;
	private String username;
	private String email;
    private List<String> roles;

    public JwtResponse(String accessToken, UUID id, String username, String email, List<String> roles) {
		this.accessToken = accessToken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}
    
}
