package com.trodix.todoapi.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import com.trodix.todoapi.security.service.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;

@Component
public class JwtService {
	private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

	@Value("${app.jwt-secret}")
	private String jwtSecret;

	@Value("${app.jwt-expiration-sec}")
	private int jwtExpirationSec;

	public String generateJwtToken(final Authentication authentication) {

		final UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		return generateJwtTokenWithUsername(userPrincipal.getUsername());
	}

	public String generateJwtTokenWithUsername(final String username) {

		final SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationSec * 1000))
				.signWith(key)
				.compact();
	}

	public String getUserNameFromJwtToken(final String token) {
		return Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(final String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(authToken);
			return true;
		} catch (final SecurityException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (final MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (final ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (final UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (final IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}
