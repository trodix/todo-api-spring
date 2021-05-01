package com.trodix.todoapi.service;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.trodix.todoapi.entity.ERole;
import com.trodix.todoapi.entity.RefreshToken;
import com.trodix.todoapi.entity.Role;
import com.trodix.todoapi.entity.User;
import com.trodix.todoapi.exception.BadRequestException;
import com.trodix.todoapi.model.request.LoginRequest;
import com.trodix.todoapi.model.request.LogoutRequest;
import com.trodix.todoapi.model.request.RefreshTokenRequest;
import com.trodix.todoapi.model.request.SignupRequest;
import com.trodix.todoapi.model.response.JwtResponse;
import com.trodix.todoapi.repository.RoleRepository;
import com.trodix.todoapi.repository.UserRepository;
import com.trodix.todoapi.security.jwt.JwtService;
import com.trodix.todoapi.security.service.RefreshTokenService;
import com.trodix.todoapi.security.service.RoleService;
import com.trodix.todoapi.security.service.UserDetailsImpl;
import com.trodix.todoapi.security.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService {

    @Value("${app.default-user.username}")
    private String defaultUser;

    @Value("${app.default-user.email}")
    private String defaultEmail;

    @Value("#{'${app.default-user.roles}'.split(',')}")
    private Set<String> defaultRoles;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
	RoleRepository roleRepository;

	@Autowired
    PasswordEncoder encoder;
    
    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    RoleService roleService;

    @Autowired
    JwtService jwtUtils;
    
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
	UserDetailsServiceImpl userDetailsService;

    /**
     * Log the user in
     * @param loginRequest
     * @return The user tokens pair
     */
    public JwtResponse authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

        RefreshToken userRefreshToken = this.refreshTokenService.getToken(loginRequest.getUsername());
        String refreshToken = null;

        if (userRefreshToken == null) {
            refreshToken = refreshTokenService.generateRefreshToken(authentication).getToken();
        } else {
            refreshToken = userRefreshToken.getToken();
        }
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

        return new JwtResponse(jwt, refreshToken, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }
    
    /**
     * Generate a new JWT Token if the given refreshToken is valid
     * @param refreshTokenRequest
     * @return The new JWT token and refreshToken pair
     */
    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken(), refreshTokenRequest.getUsername());

		UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(refreshTokenRequest.getUsername());

		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		String jwt = jwtUtils.generateJwtTokenWithUsername(userDetails.getUsername());

        return new JwtResponse(jwt, refreshTokenRequest.getRefreshToken(), userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }

    /**
     * Logout the given user and destroy his refreshToken 
     * @param logoutRequest
     */
    public void logout(LogoutRequest logoutRequest) {

		refreshTokenService.deleteRefreshToken(logoutRequest.getRefreshToken());
    }

    /**
     * Create a new user in the database with the given credentials
     * @param signupRequest
     */
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

    /**
     * Initialize the default user in the database
     */
    public void initDefaultUser() {
        try {
			// Create the default roles
			this.roleService.initDefaultRoles();

			String defaultPassword = UUID.randomUUID().toString();

			SignupRequest signupRequest = new SignupRequest();
			signupRequest.setEmail(this.defaultEmail);
			signupRequest.setUsername(this.defaultUser);
			signupRequest.setPassword(defaultPassword);
			signupRequest.setRole(this.defaultRoles);

			this.registerUser(signupRequest);

			log.info(MessageFormat.format("\n\n=============== Default credentials are: {0} / {1} ===============\n",
					this.defaultUser, defaultPassword));
		} catch (BadRequestException e) {
			// admin user has already been created
			log.info(MessageFormat.format("Error while generating the default user [{0}], skiping... {1}", this.defaultUser, e.getMessage()));
		}
    }

}