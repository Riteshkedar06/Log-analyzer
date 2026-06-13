package com.loganalyzer.platform.source.service.impl;

import com.loganalyzer.platform.common.exception.DuplicateResourceException;
import com.loganalyzer.platform.common.exception.ResourceNotFoundException;
import com.loganalyzer.platform.project.entity.Project;
import com.loganalyzer.platform.project.repository.ProjectRepository;
import com.loganalyzer.platform.source.dto.request.CreateSourceRequest;
import com.loganalyzer.platform.source.dto.request.UpdateSourceRequest;
import com.loganalyzer.platform.source.dto.response.SourceResponse;
import com.loganalyzer.platform.source.entity.Source;
import com.loganalyzer.platform.source.mapper.SourceMapper;
import com.loganalyzer.platform.source.repository.SourceRepository;
import com.loganalyzer.platform.source.service.SourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SourceServiceImpl implements SourceService {

    private final SourceRepository sourceRepository;
    private final ProjectRepository projectRepository;
    private final SourceMapper sourceMapper;

    String sourceNotFound ="Source not found";
    @Override
    public SourceResponse createSource(
            UUID projectId,
            CreateSourceRequest request
    ) {

        Project project = projectRepository
                .findByProjectId(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Project not found"
                        ));

        if (sourceRepository.existsByProject_ProjectIdAndServiceName(
                projectId,
                request.serviceName()
        )) {

            throw new DuplicateResourceException(
                    "Source already exists for this project"
            );
        }

        Source source = Source.builder()
                .project(project)
                .serviceName(request.serviceName())
                .environment(request.environment())
                .host(request.host())
                .createdAt(Instant.now())
                .build();

        sourceRepository.save(source);

        return sourceMapper.toResponse(source);
    }

    @Override
    public SourceResponse getSource(
            UUID sourceId
    ) {

        Source source = sourceRepository
                .findBySourceId(sourceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(sourceNotFound));

        return sourceMapper.toResponse(source);
    }

    @Override
    public List<SourceResponse> getSourcesByProject(
            UUID projectId
    ) {

        Project project = projectRepository
                .findByProjectId(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Project not found"
                        ));

        return sourceMapper.toResponseList(
                sourceRepository.findAllByProject_ProjectId(projectId)
        );
    }

    @Override
    public SourceResponse updateSource(
            UUID sourceId,
            UpdateSourceRequest request
    ) {

        Source source = sourceRepository
                .findBySourceId(sourceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(sourceNotFound));

        source.setServiceName(
                request.serviceName()
        );

        source.setEnvironment(
                request.environment()
        );

        source.setHost(
                request.host()
        );

        sourceRepository.save(source);

        return sourceMapper.toResponse(source);
    }

    @Override
    public void deleteSource(
            UUID sourceId
    ) {

        Source source = sourceRepository
                .findBySourceId(sourceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Source not found"));

        sourceRepository.delete(source);
    }
}