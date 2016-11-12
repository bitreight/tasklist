package com.bitreight.tasklist.service.converter.impl;

import com.bitreight.tasklist.dto.ProjectDto;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.service.converter.ProjectDtoConverter;
import org.springframework.stereotype.Component;

@Component
public class ProjectDtoConverterImpl implements ProjectDtoConverter {

    @Override
    public Project convertDto(ProjectDto projectDto) {
        Project project = new Project();
        project.setId(projectDto.getId());
        project.setTitle(projectDto.getTitle());
        project.setDescription(projectDto.getDescription());
        return project;
    }

    @Override
    public ProjectDto convertEntity(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(project.getId());
        projectDto.setTitle(project.getTitle());
        projectDto.setDescription(project.getDescription());
        return projectDto;
    }
}
