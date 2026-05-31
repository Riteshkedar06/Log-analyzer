package com.loganalyzer.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key signingKey;

    @PostConstruct
    public void init() {

        if(secret.length() < 32){
            throw new IllegalStateException(
                    "JWT secret too short"
            );
        }

        signingKey =
                Keys.hmacShaKeyFor(
                        secret.getBytes()
                );
    }

    public Claims extractClaims(
            String token
    ) {

        return Jwts.parserBuilder()
                .setSigningKey(
                        signingKey
                )
                .build()
                .parseClaimsJws(
                        token
                )
                .getBody();
    }

    public String extractEmail(
            String token
    ) {

        return extractClaims(
                token
        ).getSubject();
    }

    public String extractRole(
            String token
    ) {

        return extractClaims(token)
                .get(
                        "role",
                        String.class
                );
    }

    public boolean validate(
            String token
    ) {

        try{
            extractClaims(token);
            return true;
        }
        catch(Exception ex){
            return false;
        }
    }
}