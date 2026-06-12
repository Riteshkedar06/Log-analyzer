package com.loganalyzer.platform.agent.dto.response;

import java.util.UUID;

public record RegisterAgentResponse(

        UUID agentId,

        String apiKey

) {
}
