package com.bitreight.tasklist.service;

import com.bitreight.tasklist.dto.ProjectDto;
import com.bitreight.tasklist.service.exception.ServiceProjectAlreadyExistsException;
import com.bitreight.tasklist.service.exception.ServiceProjectNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceUserNotFoundException;

import java.util.List;

public interface ProjectService {

    int add(ProjectDto projectDto, int userId) throws ServiceProjectAlreadyExistsException, ServiceUserNotFoundException;

    void update(ProjectDto projectDto) throws ServiceProjectAlreadyExistsException, ServiceProjectNotFoundException;

    void deleteById(int projectId) throws ServiceProjectNotFoundException;

    ProjectDto getById(int projectId) throws ServiceProjectNotFoundException;

    List<ProjectDto> getByUserId(int userId) throws ServiceUserNotFoundException, ServiceProjectNotFoundException;
}
