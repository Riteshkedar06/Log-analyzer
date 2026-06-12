package com.loganalyzer.platform.agent.health;

import com.loganalyzer.platform.agent.entity.Agent;
import com.loganalyzer.platform.agent.entity.AgentStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class AgentHealthServiceImpl
        implements AgentHealthService {

    @Override
    public AgentStatus getStatus(
            Agent agent
    ) {

        if (agent.getLastHeartbeat() == null) {
            return AgentStatus.OFFLINE;
        }

        Duration duration =
                Duration.between(
                        agent.getLastHeartbeat(),
                        Instant.now()
                );

        if (duration.toMinutes() < 1) {
            return AgentStatus.ONLINE;
        }

        if (duration.toMinutes() < 5) {
            return AgentStatus.DEGRADED;
        }

        return AgentStatus.OFFLINE;
    }
}
