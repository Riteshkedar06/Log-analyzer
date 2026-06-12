package com.loganalyzer.platform.agent.dto.response;

import com.loganalyzer.platform.agent.entity.AgentStatus;

import java.time.Instant;
import java.util.UUID;

public record AgentResponse(

        UUID agentId,

        UUID sourceId,

        String hostname,

        AgentStatus status,

        Instant lastHeartbeat,

        Instant registeredAt

) {
}