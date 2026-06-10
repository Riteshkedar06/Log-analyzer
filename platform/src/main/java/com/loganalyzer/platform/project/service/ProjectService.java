package com.loganalyzer.platform.project.service;

import com.loganalyzer.platform.project.dto.request.CreateProjectRequest;
import com.loganalyzer.platform.project.dto.response.ProjectResponse;

import java.util.List;
import java.util.UUID;

public interface ProjectService {
    ProjectResponse createProject(CreateProjectRequest createProject, UUID userId);

    ProjectResponse getProject(UUID projectId);

    List<ProjectResponse> getProjectsForUser(UUID userId);
}
