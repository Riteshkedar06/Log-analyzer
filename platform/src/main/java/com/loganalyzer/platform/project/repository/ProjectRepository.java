package com.loganalyzer.platform.project.repository;

import com.loganalyzer.platform.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Optional<Project> findByProjectId(UUID projectId);

    boolean existsByProjectNameAndCreatedBy(
            String projectName,
            UUID createdBy
    );

    List<Project> findAllByCreatedBy(UUID userId);

}
