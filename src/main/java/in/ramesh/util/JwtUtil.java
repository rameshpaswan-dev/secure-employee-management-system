package in.ramesh.util;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long expirationTime; // in milliseconds

	//Secure key generation
	private Key getSigningKey() {
		
		return Keys.hmacShaKeyFor(secret.getBytes());
	}

	
	// TOKEN GENERATION
	public String generateToken(String username, String role) {

		return Jwts.builder().setSubject(username)
				.claim("role", role)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
	}

	
	// EXTRACT CLAIMS
	private Claims extractAllClaims(String token) {

		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build().parseClaimsJws(token)
				.getBody();
	}

	public String extractUsername(String token) {
		return extractAllClaims(token)
				.getSubject();
	}

	public String extractRole(String token) {
		return extractAllClaims(token)
				.get("role", String.class);
	}

	public Date extractExpiration(String token) {
		return extractAllClaims(token)
				.getExpiration();
	}

	
	// VALIDATION
	public boolean validateToken(String token) {

		try {
			extractAllClaims(token);
			return !isTokenExpired(token);

		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	
	// ⏳ EXPIRATION CHECK
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
}