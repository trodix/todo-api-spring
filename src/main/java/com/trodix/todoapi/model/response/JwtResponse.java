package com.trodix.todoapi.model.response;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

	private String accessToken;
	private String refreshToken;
	private UUID id;
	private String username;
	private String email;
    private List<String> roles;
    
}
