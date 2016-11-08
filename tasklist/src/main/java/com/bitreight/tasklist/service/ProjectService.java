package com.bitreight.tasklist.service;

import com.bitreight.tasklist.dto.ProjectDto;

import java.util.List;

public interface ProjectService {
    void add(ProjectDto projectDto, int userId);

    void update(ProjectDto projectDto);

    void deleteById(int projectId);

    ProjectDto getById(int projectId);

    List<ProjectDto> getByUser(int userId);
}
