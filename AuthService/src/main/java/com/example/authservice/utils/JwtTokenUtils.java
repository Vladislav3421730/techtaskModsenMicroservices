package com.example.authservice.utils;


import com.example.authservice.model.User;
import com.example.authservice.model.role.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Setter
@Getter
@Slf4j
public class JwtTokenUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;


    public String generateToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = user.getRoles().stream()
                .map(Role::getAuthority)
                .collect(Collectors.toList());

        claims.put("roles", rolesList);

        Date issuedDate = new Date();
        Date expirationDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());

        log.info("Generating JWT token for user: {}", user.getUsername());
        log.debug("Token issued at: {}", issuedDate);
        log.debug("Token expiration date: {}", expirationDate);

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        log.info("JWT token successfully generated for user: {}", user.getUsername());
        return token;
    }


    public String getUsername(String token) {

        return  Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


}
