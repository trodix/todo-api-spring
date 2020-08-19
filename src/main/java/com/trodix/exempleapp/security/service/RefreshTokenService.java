package com.trodix.exempleapp.security.service;

import java.time.Instant;
import java.util.UUID;

import javax.transaction.Transactional;

import com.trodix.exempleapp.entity.RefreshToken;
import com.trodix.exempleapp.entity.User;
import com.trodix.exempleapp.exception.UnauthorizedException;
import com.trodix.exempleapp.repository.RefreshTokenRepository;
import com.trodix.exempleapp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {
    
    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private final UserRepository userRepository;

    public RefreshToken generateRefreshToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userPrincipal.getId())
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + userPrincipal.getUsername()));

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setUser(user);

        return refreshTokenRepository.save(refreshToken);
    }

    public void validateRefreshToken(String refreshToken, String username) {
        refreshTokenRepository.findByTokenAndUsername(refreshToken, username)
            .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));
    }

    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }
}