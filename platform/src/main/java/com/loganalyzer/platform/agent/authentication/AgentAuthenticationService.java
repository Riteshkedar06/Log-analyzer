package com.loganalyzer.platform.agent.authentication;

import com.loganalyzer.platform.agent.entity.Agent;

public interface AgentAuthenticationService {

    Agent authenticate(
            String apiKey
    );
}
