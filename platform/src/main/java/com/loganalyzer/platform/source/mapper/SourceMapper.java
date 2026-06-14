package com.loganalyzer.platform.source.mapper;

import com.loganalyzer.platform.source.dto.response.SourceResponse;
import com.loganalyzer.platform.source.entity.Source;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SourceMapper {

    @Mapping(
            target = "projectId",
            source = "project.projectId"
    )
    SourceResponse toResponse(Source source);

    List<SourceResponse> toResponseList(
            List<Source> sources
    );
}