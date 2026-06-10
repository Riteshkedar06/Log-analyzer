package com.loganalyzer.platform.project.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateProjectRequest(
        @NotBlank
        String projectName,
        String description
) {
}
