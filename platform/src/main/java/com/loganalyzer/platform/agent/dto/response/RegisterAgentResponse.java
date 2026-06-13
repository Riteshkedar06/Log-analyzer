package com.loganalyzer.platform.agent.dto.response;

import java.util.UUID;

public record RegisterAgentResponse(

        UUID agentId,
        UUID sourceId,
        String apiKey

) {
}
