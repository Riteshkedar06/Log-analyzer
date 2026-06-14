package com.loganalyzer.platform.agent.service;

import com.loganalyzer.platform.agent.authentication.AgentAuthenticationService;
import com.loganalyzer.platform.agent.dto.request.RegisterAgentRequest;
import com.loganalyzer.platform.agent.dto.response.AgentResponse;
import com.loganalyzer.platform.agent.dto.response.RegisterAgentResponse;
import com.loganalyzer.platform.agent.entity.Agent;
import com.loganalyzer.platform.agent.entity.AgentStatus;
import com.loganalyzer.platform.agent.mapper.AgentMapper;
import com.loganalyzer.platform.agent.repository.AgentRepository;
import com.loganalyzer.platform.agent.utils.ApiKeyGenerator;
import com.loganalyzer.platform.common.exception.ResourceNotFoundException;
import com.loganalyzer.platform.source.entity.Source;
import com.loganalyzer.platform.source.repository.SourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {

    private final AgentRepository agentRepository;
    private final SourceRepository sourceRepository;
    private final AgentMapper agentMapper;
    private final ApiKeyGenerator apiKeyGenerator;
    private final AgentAuthenticationService agentAuthenticationService;

    @Override
    public RegisterAgentResponse registerAgent(
            RegisterAgentRequest request
    ) {

        Source source = sourceRepository
                .findBySourceId(request.sourceId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Source not found"
                        ));

        return agentRepository
                .findBySourceAndHostname(
                        source,
                        request.hostname()
                )
                .map(agentMapper::toRegisterResponse)
                .orElseGet(() -> {

                    Agent agent = Agent.builder()
                            .source(source)
                            .hostname(request.hostname())
                            .apiKey(apiKeyGenerator.generate())
                            .status(AgentStatus.ONLINE)
                            .registeredAt(Instant.now())
                            .lastHeartbeat(Instant.now())
                            .agentVersion(request.agentVersion())
                            .build();

                    Agent savedAgent =
                            agentRepository.save(agent);

                    return agentMapper
                            .toRegisterResponse(savedAgent);
                });
    }

    @Override
    public AgentResponse getAgent(
            UUID agentId
    ) {

        Agent agent = agentRepository
                .findById(agentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Agent not found"
                        ));

        return agentMapper.toResponse(agent);
    }

    @Override
    public List<AgentResponse> getAgentsBySource(
            UUID sourceId
    ) {

        Source source = sourceRepository
                .findBySourceId(sourceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Source not found"
                        ));

        return agentMapper.toResponseList(
                agentRepository.findAllBySource(source)
        );
    }

    @Override
    public void heartbeat(
            String apiKey
    ) {

        Agent agent =
                agentAuthenticationService
                        .authenticate(apiKey);

        agent.setLastHeartbeat(
                Instant.now()
        );

        if (agent.getStatus() != AgentStatus.ONLINE) {
            agent.setStatus(AgentStatus.ONLINE);
        }

        agentRepository.save(agent);
    }
}

