package com.loganalyzer.platform.source.repository;

import com.loganalyzer.platform.project.entity.Project;
import com.loganalyzer.platform.source.entity.Source;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SourceRepository
        extends JpaRepository<Source, UUID> {

    Optional<Source> findBySourceId(UUID sourceId);

    List<Source> findAllByProject_ProjectId(UUID projectId);

    boolean existsByProject_ProjectIdAndServiceName(
            UUID projectId,
            String serviceName
    );
}
