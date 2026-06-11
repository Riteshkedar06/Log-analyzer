package com.loganalyzer.platform.source.dto.response;

import java.time.Instant;
import java.util.UUID;

public record SourceResponse(

        UUID sourceId,
        UUID projectId,
        String serviceName,
        String environment,
        String host,
        Instant createdAt

) {
}
