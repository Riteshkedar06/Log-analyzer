package com.loganalyzer.platform.agent.entity;


import com.loganalyzer.platform.agent.dto.response.RegisterAgentResponse;
import com.loganalyzer.platform.source.entity.Source;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "agents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID agentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Source source;

    @Column(nullable = false, unique = true)
    private String apiKey;

    @Column(nullable = false)
    private String hostname;

    @Enumerated(EnumType.STRING)
    private AgentStatus status;
    private Instant lastHeartbeat;
    private Instant registeredAt;

    @Column(nullable = false)
    private String agentVersion;

}