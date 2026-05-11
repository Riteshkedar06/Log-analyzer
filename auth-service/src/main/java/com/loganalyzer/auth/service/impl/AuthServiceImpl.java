package com.loganalyzer.auth.service.impl;

import com.loganalyzer.auth.dto.request.LoginRequest;
import com.loganalyzer.auth.dto.request.RegisterRequest;
import com.loganalyzer.auth.dto.response.ApiResponse;
import com.loganalyzer.auth.entity.User;
import com.loganalyzer.auth.exception.InvalidCredentialsException;
import com.loganalyzer.auth.exception.UserAlreadyExistsException;
import com.loganalyzer.auth.repository.UserRepository;
import com.loganalyzer.auth.security.JwtService;
import com.loganalyzer.auth.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder,JwtService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public ApiResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("User already exists");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        userRepository.save(user);

        return new ApiResponse(
                "User registered successfully",
                null
        );
    }

    @Override
    public ApiResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(()->new InvalidCredentialsException("Invalid email or password"));
        boolean passwordMatches = passwordEncoder.matches(request.password(),user.getPassword());
        if(!passwordMatches){
            throw new InvalidCredentialsException("Invalid email or password");
        }
        String token = jwtService.generateToken(user.getEmail());
        return new ApiResponse(
                "Login Successful",
                token

        );
    }
}
