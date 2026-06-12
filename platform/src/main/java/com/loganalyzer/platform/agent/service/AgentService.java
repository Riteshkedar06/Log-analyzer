package com.loganalyzer.platform.agent.service;

import com.loganalyzer.platform.agent.dto.request.RegisterAgentRequest;
import com.loganalyzer.platform.agent.dto.response.AgentResponse;
import com.loganalyzer.platform.agent.dto.response.RegisterAgentResponse;

import java.util.List;
import java.util.UUID;

public interface AgentService {

    RegisterAgentResponse registerAgent(
            RegisterAgentRequest request
    );

    AgentResponse getAgent(
            UUID agentId
    );

    List<AgentResponse> getAgentsBySource(
            UUID sourceId
    );

    void heartbeat(
            String apiKey
    );
}
