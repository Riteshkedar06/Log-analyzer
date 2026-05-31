package com.loganalyzer.gateway.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    public Mono<Void> handle(
            ServerWebExchange exchange,
            String message,
            HttpStatus status
    ) {

        exchange.getResponse()
                .setStatusCode(status);

        exchange.getResponse()
                .getHeaders()
                .setContentType(
                        MediaType.APPLICATION_JSON
                );

        Map<String,Object> body =
                new HashMap<>();

        body.put(
                "timestamp",
                LocalDateTime.now()
        );

        body.put(
                "status",
                status.value()
        );

        body.put(
                "message",
                message
        );

        try {

            byte[] bytes =
                    objectMapper
                            .writeValueAsBytes(
                                    body
                            );

            return exchange
                    .getResponse()
                    .writeWith(
                            Mono.just(
                                    exchange
                                            .getResponse()
                                            .bufferFactory()
                                            .wrap(bytes)
                            )
                    );

        } catch (Exception e) {

            return Mono.error(e);
        }
    }
}