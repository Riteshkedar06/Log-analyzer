package com.loganalyzer.gateway.filter;

import com.loganalyzer.gateway.exception.GlobalExceptionHandler;
import com.loganalyzer.gateway.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
    private final GlobalExceptionHandler globalExceptionHandler;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // Skip public APIs
        if (path.startsWith("/auth") || path.startsWith("/actuator")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            return globalExceptionHandler.handle(exchange, "Missing Token", HttpStatus.UNAUTHORIZED
            );
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validate(token)) {

            return globalExceptionHandler.handle(exchange, "Invalid Token", HttpStatus.UNAUTHORIZED);
        }

        String email = jwtUtil.extractEmail(token);

        String role = jwtUtil.extractRole(token);

        ServerHttpRequest request = exchange.getRequest().mutate()

                .header("X-User-Email", email)

                .header("X-User-Role", role)

                .build();

        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return -1;
    }
}