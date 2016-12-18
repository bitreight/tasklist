package com.bitreight.tasklist.service;

import com.bitreight.tasklist.dto.TaskDto;
import com.bitreight.tasklist.service.exception.ServiceProjectNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceTaskNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceUserNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDate;
import java.util.List;

public interface TaskSearchService {

    @PreAuthorize("principal.id == #userId")
    List<TaskDto> getByUserId(int userId, String primarySortKey) throws ServiceUserNotFoundException, ServiceTaskNotFoundException;

    @PreAuthorize("@securityService.isProjectOwner(#projectId)")
    List<TaskDto> getByProjectId(int projectId, String primarySortKey)
            throws ServiceProjectNotFoundException, ServiceTaskNotFoundException;

    @PreAuthorize("principal.id == #userId")
    List<TaskDto> getTodayTasksByUserId(int userId, LocalDate clientCurrentDate, String primarySortKey)
            throws ServiceUserNotFoundException, ServiceTaskNotFoundException;

    @PreAuthorize("principal.id == #userId")
    List<TaskDto> getWeekTasksByUserId(int userId, LocalDate clientCurrentDate, String primarySortKey)
            throws ServiceUserNotFoundException, ServiceTaskNotFoundException;
}