package com.loganalyzer.platform.ingection.entity;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record RawLogEvent(

        UUID projectId,

        UUID sourceId,

        UUID agentId,

        String serviceName,

        String hostname,

        String level,

        String message,

        Instant timestamp

) {
}