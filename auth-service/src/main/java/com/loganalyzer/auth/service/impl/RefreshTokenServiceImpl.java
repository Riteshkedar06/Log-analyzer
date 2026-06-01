package com.loganalyzer.auth.service.impl;

import com.loganalyzer.auth.entity.User;
import com.loganalyzer.auth.entity.UserSession;
import com.loganalyzer.auth.exception.InvalidRefreshTokenException;
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

    private static final String REFRESH_TOKEN_PREFIX = "refresh:";

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

        String refreshToken = UUID.randomUUID().toString();

        String tokenHash = hash(refreshToken);

        UserSession session = UserSession.builder()
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

        redisTemplate.opsForValue().set(
                REFRESH_TOKEN_PREFIX + tokenHash,
                user.getEmail(),
                refreshTokenExpiration,
                TimeUnit.MILLISECONDS
        );

        log.info(
                "Refresh token created for user {}",
                user.getEmail()
        );

        return refreshToken;
    }

    @Override
    public UserSession validateRefreshToken(
            String refreshToken
    ) {

        String tokenHash = hash(refreshToken);

        Boolean exists = redisTemplate.hasKey(
                REFRESH_TOKEN_PREFIX + tokenHash
        );

        if (Boolean.FALSE.equals(exists)) {

            throw new InvalidRefreshTokenException(
                    "Invalid refresh token"
            );
        }

        UserSession session =
                userSessionRepository
                        .findByRefreshTokenHashAndRevokedFalse(
                                tokenHash
                        )
                        .orElseThrow(
                                () ->
                                        new InvalidRefreshTokenException(
                                                "Invalid refresh token"
                                        )
                        );

        if (
                session.getExpiresAt()
                        .isBefore(LocalDateTime.now())
        ) {

            throw new InvalidRefreshTokenException(
                    "Refresh token expired"
            );
        }

        return session;
    }

    @Override
    public void revokeRefreshToken(
            String refreshToken
    ) {

        String tokenHash = hash(refreshToken);

        userSessionRepository
                .findByRefreshTokenHash(tokenHash)
                .ifPresent(session -> {

                    session.setRevoked(true);

                    userSessionRepository.save(session);

                    log.info(
                            "Refresh token revoked for user {}",
                            session.getUser().getEmail()
                    );
                });

        redisTemplate.delete(
                REFRESH_TOKEN_PREFIX + tokenHash
        );
    }

    @Override
    public void revokeAllUserSessions(
            User user
    ) {

        List<UserSession> sessions =
                userSessionRepository
                        .findByUserAndRevokedFalse(user);

        sessions.forEach(
                session -> session.setRevoked(true)
        );

        userSessionRepository.saveAll(sessions);

        sessions.forEach(session ->
                redisTemplate.delete(
                        REFRESH_TOKEN_PREFIX
                                + session.getRefreshTokenHash()
                )
        );

        log.info(
                "All sessions revoked for user {}",
                user.getEmail()
        );
    }

    @Override
    public String rotateRefreshToken(
            String oldRefreshToken
    ) {

        UserSession session =
                validateRefreshToken(
                        oldRefreshToken
                );

        revokeRefreshToken(
                oldRefreshToken
        );

        return createRefreshToken(
                session.getUser(),
                session.getDeviceName(),
                session.getIpAddress()
        );
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

            return HexFormat.of()
                    .formatHex(hash);

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Unable to hash token",
                    ex
            );
        }
    }
}