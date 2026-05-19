package com.loganalyzer.auth.service.impl;

import com.loganalyzer.auth.dto.request.LoginRequest;
import com.loganalyzer.auth.dto.request.RegisterRequest;
import com.loganalyzer.auth.dto.response.ApiResponse;
import com.loganalyzer.auth.entity.AuthProvider;
import com.loganalyzer.auth.entity.Role;
import com.loganalyzer.auth.entity.User;
import com.loganalyzer.auth.exception.InvalidCredentialsException;
import com.loganalyzer.auth.exception.UserAlreadyExistsException;
import com.loganalyzer.auth.repository.UserRepository;
import com.loganalyzer.auth.security.JwtService;
import com.loganalyzer.auth.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final JwtService jwtService;


    @Override
    public ApiResponse register(RegisterRequest request) {

        String email = request.email().trim().toLowerCase();

        log.info("Registration request for {}", email);


        if (userRepository.existsByEmail(email)) {

            throw new UserAlreadyExistsException("Email already registered");
        }


        if (userRepository.existsByUsername(request.username())) {

            throw new UserAlreadyExistsException("Username already taken");
        }


        User user = User.builder().username(request.username()).email(email).provider(AuthProvider.LOCAL).role(Role.ROLE_USER).password(passwordEncoder.encode(request.password())).build();


        userRepository.save(user);

        log.info("User registered successfully: {}", email);

        return new ApiResponse("User registered successfully", null);
    }


    @Override
    public ApiResponse login(LoginRequest request) {

        String email = request.email().trim().toLowerCase();

        log.info("Login request for {}", email);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));


        boolean passwordMatches = passwordEncoder.matches(request.password(), user.getPassword());


        if (!passwordMatches) {

            log.warn("Failed login attempt for {}", email);

            throw new InvalidCredentialsException("Invalid email or password");
        }


        String token = jwtService.generateToken(user);


        log.info("Login successful for {}", email);

        return new ApiResponse("Login successful", token);

    }
}