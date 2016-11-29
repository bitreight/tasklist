package com.bitreight.tasklist.service;

import com.bitreight.tasklist.dto.ProjectDto;
import com.bitreight.tasklist.service.exception.ServiceProjectAlreadyExistsException;
import com.bitreight.tasklist.service.exception.ServiceProjectNotFoundException;

import java.util.List;

public interface ProjectService {

    void add(ProjectDto projectDto, int userId) throws ServiceProjectAlreadyExistsException;

    void update(ProjectDto projectDto) throws ServiceProjectAlreadyExistsException, ServiceProjectNotFoundException;

    void deleteById(int projectId);

    ProjectDto getById(int projectId);

    List<ProjectDto> getByUserId(int userId);
}
