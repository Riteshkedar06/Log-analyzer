package com.loganalyzer.platform.source.controller;

import com.loganalyzer.platform.source.dto.request.CreateSourceRequest;
import com.loganalyzer.platform.source.dto.request.UpdateSourceRequest;
import com.loganalyzer.platform.source.dto.response.SourceResponse;
import com.loganalyzer.platform.source.service.SourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SourceController {

    private final SourceService sourceService;

    @PostMapping("/projects/{projectId}/sources")
    @ResponseStatus(HttpStatus.CREATED)
    public SourceResponse createSource(
            @PathVariable UUID projectId,
            @RequestBody @Valid CreateSourceRequest request
    ) {

        return sourceService.createSource(
                projectId,
                request
        );
    }

    @GetMapping("/sources/{sourceId}")
    public SourceResponse getSource(
            @PathVariable UUID sourceId
    ) {

        return sourceService.getSource(sourceId);
    }

    @GetMapping("/projects/{projectId}/sources")
    public List<SourceResponse> getSourcesByProject(
            @PathVariable UUID projectId
    ) {

        return sourceService.getSourcesByProject(projectId);
    }

    @PutMapping("/sources/{sourceId}")
    public SourceResponse updateSource(
            @PathVariable UUID sourceId,
            @RequestBody @Valid UpdateSourceRequest request
    ) {

        return sourceService.updateSource(
                sourceId,
                request
        );
    }

    @DeleteMapping("/sources/{sourceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSource(
            @PathVariable UUID sourceId
    ) {

        sourceService.deleteSource(sourceId);
    }
}