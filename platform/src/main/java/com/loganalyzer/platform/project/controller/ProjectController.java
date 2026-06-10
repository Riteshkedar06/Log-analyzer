package com.loganalyzer.platform.project.controller;

import com.loganalyzer.platform.project.dto.request.CreateProjectRequest;
import com.loganalyzer.platform.project.dto.response.ProjectResponse;
import com.loganalyzer.platform.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
public class ProjectController {
    private final ProjectService projectService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse createProject(
            @RequestBody @Valid CreateProjectRequest request,
            @RequestHeader("X-User-Id") UUID userId
    ) {

        return projectService.createProject(
                request,
                userId
        );
    }

    @GetMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectResponse getProject(
             @PathVariable  UUID projectId
    ) {

        return projectService.getProject(projectId);
    }
    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ProjectResponse> getProjectsForUser(
           @PathVariable UUID userId
    ) {

        return projectService.getProjectsForUser(userId);
    }
}
