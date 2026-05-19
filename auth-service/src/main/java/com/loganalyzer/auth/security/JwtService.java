package com.loganalyzer.auth.security;

import com.loganalyzer.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key signingKey;


    @PostConstruct
    public void init() {

        if(secret.length()<32){

            throw new IllegalStateException(
                    "JWT secret too short"
            );
        }

        signingKey=
                Keys.hmacShaKeyFor(
                        secret.getBytes()
                );
    }


    public String generateToken(
            User user
    ) {

        return Jwts.builder()
                .setSubject(
                        user.getEmail()
                )
                .claim(
                        "role",
                        user.getRole().name()
                )
                .setIssuedAt(
                        new Date()
                )
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + expiration
                        )
                )
                .signWith(
                        signingKey,
                        SignatureAlgorithm.HS256
                )
                .compact();
    }


    public String extractEmail(
            String token
    ){

        return extractClaims(
                token
        ).getSubject();
    }


    public String extractRole(
            String token
    ){

        return extractClaims(token)
                .get(
                        "role",
                        String.class
                );
    }


    public boolean isTokenValid(
            String token,
            UserDetails userDetails
    ){

        Claims claims=
                extractClaims(token);

        return claims
                .getSubject()
                .equals(
                        userDetails.getUsername()
                )
                &&
                !claims
                        .getExpiration()
                        .before(
                                new Date()
                        );
    }


    private Claims extractClaims(
            String token
    ){

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

}