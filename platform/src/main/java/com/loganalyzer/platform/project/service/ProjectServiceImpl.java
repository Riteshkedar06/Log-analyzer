package com.loganalyzer.platform.project.service;

import com.loganalyzer.platform.common.exception.DuplicateResourceException;
import com.loganalyzer.platform.common.exception.ResourceNotFoundException;
import com.loganalyzer.platform.project.dto.request.CreateProjectRequest;
import com.loganalyzer.platform.project.dto.response.ProjectResponse;
import com.loganalyzer.platform.project.entity.Project;
import com.loganalyzer.platform.project.mapper.ProjectMapper;
import com.loganalyzer.platform.project.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public  class ProjectServiceImpl  implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Transactional()
    @Override
    public ProjectResponse createProject(CreateProjectRequest createProject, UUID userId) {
        if(projectRepository.existsByProjectNameAndCreatedBy(
                createProject.projectName(),
                userId
        )) {
            throw new DuplicateResourceException(
                   String.format(
                           "Project '%s' already exists for this user",
                           createProject.projectName()
                   )
            );
        }
        Project project = Project.builder()
                .projectName(createProject.projectName())
                .description(createProject.description())
                .createdBy(userId)
                .build();

        Project savedProject = projectRepository.save(project);
        return projectMapper.toResponse(savedProject);
    }

    @Override
    public ProjectResponse getProject(UUID projectId) {
        Project project = projectRepository
                .findByProjectId(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project not found"));

        return projectMapper.toResponse(project);
    }

    @Override
    public List<ProjectResponse> getProjectsForUser(UUID userId) {
        return projectRepository.findAllByCreatedBy(userId)
                .stream()
                .map(projectMapper::toResponse)
                .toList();
    }

}
