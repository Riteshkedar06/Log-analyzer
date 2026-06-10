package com.loganalyzer.platform.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(
            ResourceNotFoundException ex
    ) {

        return new ErrorResponse(
                "RESOURCE_NOT_FOUND",
                ex.getMessage(),
                Instant.now()
        );
    }
    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDuplicate(
            DuplicateResourceException ex
    ) {

        return new ErrorResponse(
            "RESOURCE_DUPLICATE",
                ex.getMessage(),
                Instant.now()
        );
    }
}