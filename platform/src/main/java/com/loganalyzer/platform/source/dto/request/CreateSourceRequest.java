package com.loganalyzer.platform.source.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateSourceRequest(

        @NotBlank
        String serviceName,

        String environment,

        String host

) {}
