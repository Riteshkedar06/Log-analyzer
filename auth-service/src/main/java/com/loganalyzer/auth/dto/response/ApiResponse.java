package com.loganalyzer.auth.dto.response;

public record ApiResponse(
        String message,
        Object data
) {
}

