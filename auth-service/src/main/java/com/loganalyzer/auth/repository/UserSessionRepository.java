package com.loganalyzer.auth.repository;

import com.loganalyzer.auth.entity.User;
import com.loganalyzer.auth.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSessionRepository
        extends JpaRepository<UserSession, Long> {

    List<UserSession> findByUserAndRevokedFalse(
            User user
    );

    Optional<UserSession> findByRefreshTokenHashAndRevokedFalse(
            String refreshTokenHash
    );
    Optional<UserSession> findByRefreshTokenHash(
            String refreshTokenHash
    );

}