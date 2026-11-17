
package com.floras.cafe.security;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.guest-expiration:3600000}")
    private long guestJwtExpiration;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate JWT token -> customer/admin
    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, jwtExpiration);
    }

    // Generate JWT token -> guest
    public String generateGuestToken(UserDetails userDetails) {
        return generateToken(userDetails, guestJwtExpiration);
    }

    // Common JWT generation
    private String generateToken(
            UserDetails userDetails,
            long expirationTime) {

        Date issuedAt = new Date();

        Date expiration = new Date(
                issuedAt.getTime() + expirationTime);

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", roles)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    // Extract username from JWT
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Extract roles
    public List<String> extractRoles(String token) {
        return extractAllClaims(token).get("roles", List.class);
    }

    // Check guest token
    public boolean isGuestToken(String token) {

        List<String> roles = extractRoles(token);

        return roles != null
                && roles.size() == 1
                && roles.contains("ROLE_GUEST");
    }

    // Extract claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Validate JWT token
    public boolean isTokenValid(
            String token,
            UserDetails userDetails) {

        Claims claims = extractAllClaims(token);
        // final String username = extractUsername(token);
        List<String> tokenRoles = extractRoles(token);

        List<String> userRoles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return claims.getSubject().equals(userDetails.getUsername())
                && claims.getExpiration().after(new Date())
                && tokenRoles != null
                && tokenRoles.equals(userRoles);
    }

}
