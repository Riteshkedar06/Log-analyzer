package com.loganalyzer.platform.project.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;


public record ProjectResponse(
    UUID projectId,
    String projectName,
    String description,
    UUID createdBy,
    LocalDateTime createdAt
) {}

