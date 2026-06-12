package com.loganalyzer.platform.agent.dto.request;

import java.util.UUID;

public record RegisterAgentRequest(

        UUID sourceId,

        String hostname

) {
}