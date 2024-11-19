package com.example.apigateway.utils;

import com.example.apigateway.exception.InvalidJwtTokenException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;


    public List<String> getRolesFromToken(String token) {
        Claims claims = parseClaims(token);

        log.info("Receiving user roles {}",claims);

        return claims.get("roles", List.class);
    }

    private Claims parseClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void validateToken(String token) {
        try {

            Claims claims = parseClaims(token);

            if (claims.getExpiration().before(new Date())) {
                throw new ExpiredJwtException(null, claims, "Token has expired");
            }

            log.info("JWT Token is valid.");

        } catch (ExpiredJwtException e) {

            log.error("Expired JWT token: {}", e.getMessage());
            throw new InvalidJwtTokenException("Expired JWT token");

        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new InvalidJwtTokenException("Invalid JWT token");

        }
    }
}
