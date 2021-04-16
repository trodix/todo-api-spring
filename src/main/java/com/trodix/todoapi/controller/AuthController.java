package com.trodix.todoapi.controller;

import javax.validation.Valid;

import com.trodix.todoapi.model.request.LoginRequest;
import com.trodix.todoapi.model.request.LogoutRequest;
import com.trodix.todoapi.model.request.RefreshTokenRequest;
import com.trodix.todoapi.model.request.SignupRequest;
import com.trodix.todoapi.model.response.JwtResponse;
import com.trodix.todoapi.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/auth")
public class AuthController {

	@Autowired
	AuthService authService;

	@PostMapping("/signin")
	public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		return authService.authenticateUser(loginRequest);
	}

	@PostMapping("/refresh-token")
	public JwtResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
		return authService.refreshToken(refreshTokenRequest);
	}

	@PostMapping("/logout")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void logout(@Valid @RequestBody LogoutRequest logoutRequest) {
		authService.logout(logoutRequest);
	}

	@PostMapping("/signup")
	public void registerUser(@Valid @RequestBody SignupRequest signupRequest) {
		authService.registerUser(signupRequest);
	}
}
