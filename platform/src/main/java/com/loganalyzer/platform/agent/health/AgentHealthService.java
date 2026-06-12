package com.loganalyzer.platform.agent.health;

import com.loganalyzer.platform.agent.entity.Agent;
import com.loganalyzer.platform.agent.entity.AgentStatus;

public interface AgentHealthService {
    AgentStatus getStatus(
            Agent agent
    );
}
