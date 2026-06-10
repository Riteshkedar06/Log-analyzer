package com.loganalyzer.platform.project.dto.response;

import java.time.Instant;
import java.util.UUID;

public class ProjectResponse {
    UUID projectId;
    String projectName;
    String description;
    UUID createdBy;
    Instant createdAt;
}

