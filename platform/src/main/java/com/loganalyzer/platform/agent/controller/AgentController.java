package com.loganalyzer.platform.agent.controller;


import com.loganalyzer.platform.agent.dto.request.RegisterAgentRequest;
import com.loganalyzer.platform.agent.dto.response.AgentResponse;
import com.loganalyzer.platform.agent.dto.response.RegisterAgentResponse;
import com.loganalyzer.platform.agent.service.AgentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AgentController {

    private final AgentService agentService;

    @PostMapping("/agents/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterAgentResponse registerAgent(
            @RequestBody @Valid RegisterAgentRequest request
    ) {

        return agentService.registerAgent(request);
    }

    @PostMapping("/agents/heartbeat")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void heartbeat(
            @RequestHeader("X-Agent-Key") String apiKey
    ) {

        agentService.heartbeat(apiKey);
    }

    @GetMapping("/agents/{agentId}")
    public AgentResponse getAgent(
            @PathVariable UUID agentId
    ) {

        return agentService.getAgent(agentId);
    }

    @GetMapping("/sources/{sourceId}/agents")
    public List<AgentResponse> getAgentsBySource(
            @PathVariable UUID sourceId
    ) {

        return agentService.getAgentsBySource(sourceId);
    }
}
