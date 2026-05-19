package com.loganalyzer.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loganalyzer.auth.dto.response.ApiResponse;
import com.loganalyzer.auth.entity.AuthProvider;
import com.loganalyzer.auth.entity.Role;
import com.loganalyzer.auth.entity.User;
import com.loganalyzer.auth.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler
        implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final ObjectMapper objectMapper;


    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oauthUser =
                (OAuth2User) authentication.getPrincipal();

        String email =
                oauthUser
                        .getAttribute("email");

        String username =
                oauthUser
                        .getAttribute("name");

        log.info(
                "OAuth login for {}",
                email
        );

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseGet(() -> {

                            log.info(
                                    "Creating new OAuth user {}",
                                    email
                            );

                            return userRepository.save(
                                    User.builder()
                                            .email(email)
                                            .username(username)
                                            .password(null)
                                            .provider(
                                                    AuthProvider.GOOGLE
                                            )
                                            .role(
                                                    Role.ROLE_USER
                                            )
                                            .build()
                            );

                        });


        String token =
                jwtService.generateToken(
                        user
                );


        response.setStatus(
                HttpServletResponse.SC_OK
        );

        response.setContentType(
                "application/json"
        );

        objectMapper.writeValue(
                response.getWriter(),
                new ApiResponse(
                        "OAuth login successful",
                        token
                )
        );

    }
}