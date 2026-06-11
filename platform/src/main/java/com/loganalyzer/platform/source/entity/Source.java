package com.loganalyzer.platform.source.entity;


import com.loganalyzer.platform.project.entity.Project;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "sources")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Source {

    @Id
    private UUID sourceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(nullable = false)
    private String serviceName;

    private String environment;

    private String host;

    private Instant createdAt;
}