package com.loganalyzer.platform.agent.repository;

import com.loganalyzer.platform.agent.dto.response.AgentResponse;
import com.loganalyzer.platform.agent.entity.Agent;
import com.loganalyzer.platform.source.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AgentRepository extends JpaRepository<Agent, UUID> {
    AgentResponse findByApiKey(String apiKey);

    Agent findBySourceAndHostname(Source source, String hostname);

    List<Agent> findAllBySource(Source source);
}
