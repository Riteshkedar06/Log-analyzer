package com.loganalyzer.platform.project.mapper;

import com.loganalyzer.platform.project.dto.response.ProjectResponse;
import com.loganalyzer.platform.project.entity.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectResponse toResponse(Project project);
}
