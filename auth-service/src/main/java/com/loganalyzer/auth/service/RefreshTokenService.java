package com.loganalyzer.auth.service;

import com.loganalyzer.auth.entity.User;
import com.loganalyzer.auth.entity.UserSession;

public interface RefreshTokenService {

    String createRefreshToken(
            User user,
            String deviceName,
            String ipAddress
    );

    UserSession validateRefreshToken(
            String refreshToken
    );

    void revokeRefreshToken(
            String refreshToken
    );

    void revokeAllUserSessions(
            User user
    );
}