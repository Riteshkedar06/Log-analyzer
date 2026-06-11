package com.loganalyzer.platform.source.service;

import com.loganalyzer.platform.source.dto.request.CreateSourceRequest;
import com.loganalyzer.platform.source.dto.request.UpdateSourceRequest;
import com.loganalyzer.platform.source.dto.response.SourceResponse;

import java.util.List;
import java.util.UUID;

public interface SourceService {
    SourceResponse createSource(
            UUID projectId,
            CreateSourceRequest request
    );

    SourceResponse getSource(
            UUID sourceId
    );

    List<SourceResponse> getSourcesByProject(
            UUID projectId
    );

    SourceResponse updateSource(
            UUID sourceId,
            UpdateSourceRequest request
    );

    void deleteSource(
            UUID sourceId
    );
}
