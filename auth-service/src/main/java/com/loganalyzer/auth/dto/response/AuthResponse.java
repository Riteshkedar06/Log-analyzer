package com.loganalyzer.auth.dto.response;



public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
