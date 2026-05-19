package com.loganalyzer.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request
    ) {

        String path=request.getServletPath();

        return path.startsWith("/auth")
                || path.startsWith("/oauth2")
                || path.startsWith("/login/oauth2")
                || path.startsWith("/health")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {

            final String authHeader =
                    request.getHeader("Authorization");

            if(authHeader==null ||
                    !authHeader.startsWith("Bearer ")){

                filterChain.doFilter(
                        request,
                        response
                );

                return;
            }

            String jwt=
                    authHeader.substring(7);

            String email=
                    jwtService.extractEmail(jwt);

            if(email!=null &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication()==null){

                UserDetails userDetails=
                        userDetailsService
                                .loadUserByUsername(email);

                boolean valid=
                        jwtService.isTokenValid(
                                jwt,
                                userDetails
                        );

                if(valid){

                    UsernamePasswordAuthenticationToken authToken=
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authToken);

                    log.debug(
                            "User authenticated: {}",
                            email
                    );
                }
            }

        } catch(Exception ex){

            log.warn(
                    "JWT authentication failed: {}",
                    ex.getMessage()
            );

        }

        filterChain.doFilter(
                request,
                response
        );
    }
}