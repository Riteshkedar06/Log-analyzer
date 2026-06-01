package com.loganalyzer.auth.controller;

import com.loganalyzer.auth.dto.request.LoginRequest;
import com.loganalyzer.auth.dto.request.LogoutRequest;
import com.loganalyzer.auth.dto.request.RefreshTokenRequest;
import com.loganalyzer.auth.dto.request.RegisterRequest;
import com.loganalyzer.auth.dto.response.ApiResponse;
import com.loganalyzer.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Authentication",
        description = "Authentication APIs"
)
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Register User"
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(
            summary = "Login User",
            description = "Authenticate user and return access and refresh tokens"
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(
            summary = "Refresh Token",
            description = "Refresh the Token"
    )
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refreshToken(
            @Valid
            @RequestBody
            RefreshTokenRequest request
    ) {

        return ResponseEntity.ok(
                authService.refreshToken(
                        request
                )
        );
    }

    @Operation(
            summary = "Logout User"
    )
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(
            @Valid
            @RequestBody
            LogoutRequest request
    ) {

        return ResponseEntity.ok(
                authService.logout(request)
        );
    }

}