package com.loganalyzer.platform.agent.authentication;

import com.loganalyzer.platform.agent.authentication.AgentAuthenticationService;
import com.loganalyzer.platform.agent.entity.Agent;
import com.loganalyzer.platform.agent.repository.AgentRepository;
import com.loganalyzer.platform.common.exception.AgentAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgentAuthenticationServiceImpl
        implements AgentAuthenticationService {

    private final AgentRepository agentRepository;

    @Override
    public Agent authenticate(
            String apiKey
    ) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new AgentAuthenticationException(
                    "Agent key is required"
            );
        }

        return agentRepository
                .findByApiKey(apiKey)
                .orElseThrow(() ->
                        new AgentAuthenticationException(
                                "Invalid agent key"
                        ));
    }
}