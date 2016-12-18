package com.bitreight.tasklist.service;

import com.bitreight.tasklist.dto.TaskDto;
import com.bitreight.tasklist.service.exception.ServiceProjectNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceTaskAlreadyExistsException;
import com.bitreight.tasklist.service.exception.ServiceTaskNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceTaskVersionIsOutdatedException;
import org.springframework.security.access.prepost.PreAuthorize;

public interface TaskService {

    @PreAuthorize("@securityService.isProjectOwner(#projectId)")
    int add(TaskDto taskDto, int projectId) throws ServiceTaskAlreadyExistsException, ServiceProjectNotFoundException;

    @PreAuthorize("@securityService.isTaskOwner(#taskDto.id)")
    void update(TaskDto taskDto) throws ServiceTaskVersionIsOutdatedException, ServiceTaskAlreadyExistsException, ServiceTaskNotFoundException;

    @PreAuthorize("@securityService.isTaskOwner(#taskId)")
    void deleteById(int taskId) throws ServiceTaskNotFoundException;

    @PreAuthorize("@securityService.isTaskOwner(#taskId)")
    TaskDto getById(int taskId) throws ServiceTaskNotFoundException;

    @PreAuthorize("@securityService.isTaskOwner(#taskId)")
    void setIsCompleted(int taskId, boolean isCompleted) throws ServiceTaskNotFoundException;
}