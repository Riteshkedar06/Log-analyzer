package com.loganalyzer.auth.service;

import com.loganalyzer.auth.dto.request.LoginRequest;
import com.loganalyzer.auth.dto.request.RegisterRequest;
import com.loganalyzer.auth.dto.response.ApiResponse;

public interface AuthService {
    ApiResponse register(RegisterRequest request);

    ApiResponse login(LoginRequest request);
}
