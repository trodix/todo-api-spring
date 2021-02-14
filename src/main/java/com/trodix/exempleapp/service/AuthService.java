package com.trodix.exempleapp.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.trodix.exempleapp.entity.ERole;
import com.trodix.exempleapp.entity.Role;
import com.trodix.exempleapp.entity.User;
import com.trodix.exempleapp.exception.BadRequestException;
import com.trodix.exempleapp.model.request.LoginRequest;
import com.trodix.exempleapp.model.request.LogoutRequest;
import com.trodix.exempleapp.model.request.RefreshTokenRequest;
import com.trodix.exempleapp.model.request.SignupRequest;
import com.trodix.exempleapp.model.response.JwtResponse;
import com.trodix.exempleapp.repository.RoleRepository;
import com.trodix.exempleapp.repository.UserRepository;
import com.trodix.exempleapp.security.jwt.JwtService;
import com.trodix.exempleapp.security.service.RefreshTokenService;
import com.trodix.exempleapp.security.service.UserDetailsImpl;
import com.trodix.exempleapp.security.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;
    
    @Autowired
	RoleRepository roleRepository;

	@Autowired
    PasswordEncoder encoder;
    
    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    JwtService jwtUtils;
    
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
	UserDetailsServiceImpl userDetailsService;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		String refreshToken = refreshTokenService.generateRefreshToken(authentication).getToken();
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

        return new JwtResponse(jwt, refreshToken, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }
    
    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken(), refreshTokenRequest.getUsername());

		UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(refreshTokenRequest.getUsername());

		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		String jwt = jwtUtils.generateJwtTokenWithUsername(userDetails.getUsername());

        return new JwtResponse(jwt, refreshTokenRequest.getRefreshToken(), userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }

    public void logout(LogoutRequest logoutRequest) {

		refreshTokenService.deleteRefreshToken(logoutRequest.getRefreshToken());
    }
    
    public void registerUser(SignupRequest signupRequest) {

        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new BadRequestException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new BadRequestException("Error: Email is already in use!");
        }

        // Create new user's account
        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(), 
            encoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new BadRequestException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {

                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new BadRequestException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;

                    case "moderator":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new BadRequestException("Error: Role is not found."));
                        roles.add(modRole);
                        break;

                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new BadRequestException("Error: Role is not found."));
                        roles.add(userRole);

                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

    }

}