package com.bitreight.tasklist.service;

import com.bitreight.tasklist.dto.TaskDto;
import com.bitreight.tasklist.service.exception.ServiceProjectNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceTaskNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceUserNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface TaskSearchService {

    List<TaskDto> getByUserId(int userId, String primarySortKey) throws ServiceUserNotFoundException, ServiceTaskNotFoundException;

    List<TaskDto> getByProjectId(int projectId, String primarySortKey)
            throws ServiceProjectNotFoundException, ServiceTaskNotFoundException;

    List<TaskDto> getTodayTasksByUserId(int userId, LocalDate clientCurrentDate, String primarySortKey)
            throws ServiceUserNotFoundException, ServiceTaskNotFoundException;

    List<TaskDto> getWeekTasksByUserId(int userId, LocalDate clientCurrentDate, String primarySortKey)
            throws ServiceUserNotFoundException, ServiceTaskNotFoundException;
}
