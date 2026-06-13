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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "source_id")
    private UUID sourceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    private String environment;

    private String host;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}