package com.loganalyzer.auth.service.impl;

import com.loganalyzer.auth.entity.User;
import com.loganalyzer.auth.entity.UserSession;
import com.loganalyzer.auth.repository.UserSessionRepository;
import com.loganalyzer.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl
        implements RefreshTokenService {

    private final UserSessionRepository userSessionRepository;

    private final StringRedisTemplate redisTemplate;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;


    @Override
    public String createRefreshToken(
            User user,
            String deviceName,
            String ipAddress
    ) {

        String refreshToken =
                UUID.randomUUID().toString();

        String tokenHash =
                hash(refreshToken);

        UserSession session =
                UserSession.builder()
                        .user(user)
                        .refreshTokenHash(tokenHash)
                        .deviceName(deviceName)
                        .ipAddress(ipAddress)
                        .createdAt(LocalDateTime.now())
                        .expiresAt(
                                LocalDateTime.now()
                                        .plusSeconds(
                                                refreshTokenExpiration / 1000
                                        )
                        )
                        .revoked(false)
                        .build();

        userSessionRepository.save(session);

        redisTemplate.opsForValue()
                .set(
                        "refresh:" + tokenHash,
                        user.getEmail(),
                        refreshTokenExpiration,
                        TimeUnit.MILLISECONDS
                );

        return refreshToken;
    }


    @Override
    public UserSession validateRefreshToken(
            String refreshToken
    ) {

        String tokenHash =
                hash(refreshToken);

        Boolean exists =
                redisTemplate.hasKey(
                        "refresh:" + tokenHash
                );

        if(Boolean.FALSE.equals(exists)){
            throw new RuntimeException(
                    "Invalid refresh token"
            );
        }

        return userSessionRepository
                .findByRefreshTokenHashAndRevokedFalse(
                        tokenHash
                )
                .orElseThrow(
                        () -> new RuntimeException(
                                "Invalid refresh token"
                        )
                );
    }


    @Override
    public void revokeRefreshToken(
            String refreshToken
    ) {

        String tokenHash =
                hash(refreshToken);

        userSessionRepository
                .findByRefreshTokenHash(tokenHash)
                .ifPresent(session -> {

                    session.setRevoked(true);

                    userSessionRepository.save(session);
                });

        redisTemplate.delete(
                "refresh:" + tokenHash
        );
    }


    @Override
    public void revokeAllUserSessions(
            User user
    ) {

        List<UserSession> sessions =
                userSessionRepository
                        .findByUserAndRevokedFalse(user);

        for(UserSession session : sessions){

            session.setRevoked(true);

            userSessionRepository.save(session);

            redisTemplate.delete(
                    "refresh:"
                            + session.getRefreshTokenHash()
            );
        }
    }


    private String hash(
            String value
    ) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance(
                            "SHA-256"
                    );

            byte[] hash =
                    digest.digest(
                            value.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            return HexFormat
                    .of()
                    .formatHex(hash);

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Unable to hash token",
                    ex
            );
        }
    }
}