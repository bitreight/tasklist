package com.bitreight.tasklist.service;

import com.bitreight.tasklist.dto.ProjectDto;
import com.bitreight.tasklist.service.exception.ServiceProjectAlreadyExistsException;
import com.bitreight.tasklist.service.exception.ServiceProjectNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceUserNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ProjectService {

    @PreAuthorize("principal.id == #userId")
    int add(ProjectDto projectDto, int userId) throws ServiceProjectAlreadyExistsException, ServiceUserNotFoundException;

    @PreAuthorize("@securityService.isProjectOwner(#projectDto.id)")
    void update(ProjectDto projectDto) throws ServiceProjectAlreadyExistsException, ServiceProjectNotFoundException;

    @PreAuthorize("@securityService.isProjectOwner(#projectId)")
    void deleteById(int projectId) throws ServiceProjectNotFoundException;

    @PreAuthorize("@securityService.isProjectOwner(#projectId)")
    ProjectDto getById(int projectId) throws ServiceProjectNotFoundException;

    @PreAuthorize("principal.id == #userId")
    List<ProjectDto> getByUserId(int userId) throws ServiceUserNotFoundException, ServiceProjectNotFoundException;
}
