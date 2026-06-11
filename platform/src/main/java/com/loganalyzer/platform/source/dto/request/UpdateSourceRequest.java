package com.loganalyzer.platform.source.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateSourceRequest(

        @NotBlank
        String serviceName,

        String environment,

        String host

) {
}