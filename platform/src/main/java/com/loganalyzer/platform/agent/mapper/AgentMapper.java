package com.loganalyzer.platform.agent.mapper;

import com.loganalyzer.platform.agent.dto.response.AgentResponse;
import com.loganalyzer.platform.agent.dto.response.RegisterAgentResponse;
import com.loganalyzer.platform.agent.entity.Agent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AgentMapper {
    @Mapping(
            target = "sourceId",
            source = "source.sourceId"
    )
    RegisterAgentResponse toRegisterResponse(Agent agent);

    @Mapping(
            target = "sourceId",
            source = "source.sourceId"
    )
    AgentResponse toResponse(Agent agent);

    List<AgentResponse> toResponseList(
            List<Agent> agents
    );
}
