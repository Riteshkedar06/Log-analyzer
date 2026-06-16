package com.loganalyzer.platform.ingection.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record LogIngestionRequest(

        @NotBlank
        String level,

        @NotBlank
        String message,

        Instant timestamp

) {
}