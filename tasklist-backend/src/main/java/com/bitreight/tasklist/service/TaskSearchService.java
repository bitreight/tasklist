package com.bitreight.tasklist.service;

import com.bitreight.tasklist.dto.TaskDto;
import com.bitreight.tasklist.service.exception.ServiceNoSuchSortKeyException;
import com.bitreight.tasklist.service.exception.ServiceProjectNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceTaskNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceUserNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface TaskSearchService {

    List<TaskDto> getByProjectId(int projectId, String sortType)
            throws ServiceProjectNotFoundException, ServiceNoSuchSortKeyException, ServiceTaskNotFoundException;

    List<TaskDto> getTodayTasksByUserId(int userId, LocalDate clientCurrentDate, String sortType)
            throws ServiceNoSuchSortKeyException, ServiceUserNotFoundException, ServiceTaskNotFoundException;

    List<TaskDto> getWeekTasksByUserId(int userId, LocalDate clientCurrentDate, String sortType)
            throws ServiceNoSuchSortKeyException, ServiceUserNotFoundException, ServiceTaskNotFoundException;
}
