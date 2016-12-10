package com.bitreight.tasklist.service;

import com.bitreight.tasklist.dto.TaskDto;
import com.bitreight.tasklist.service.exception.ServiceNoSuchSortTypeException;
import com.bitreight.tasklist.service.exception.ServiceProjectNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceTaskAlreadyExistsException;
import com.bitreight.tasklist.service.exception.ServiceTaskNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceTaskVersionIsOutdatedException;
import com.bitreight.tasklist.service.exception.ServiceUserNotFoundException;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    int add(TaskDto taskDto, int projectId) throws ServiceTaskAlreadyExistsException, ServiceProjectNotFoundException;

    void update(TaskDto taskDto) throws ServiceTaskVersionIsOutdatedException, ServiceTaskAlreadyExistsException, ServiceTaskNotFoundException;

    void deleteById(int taskId) throws ServiceTaskNotFoundException;

    TaskDto getById(int taskId) throws ServiceTaskNotFoundException;

    void setIsCompleted(int taskId, boolean isCompleted) throws ServiceTaskNotFoundException;

    List<TaskDto> getByProjectId(int projectId, String sortType) throws ServiceProjectNotFoundException, ServiceNoSuchSortTypeException, ServiceTaskNotFoundException;

    List<TaskDto> getTodayTasksByUserId(int userId, LocalDate clientCurrentDate, String sortType) throws ServiceNoSuchSortTypeException, ServiceUserNotFoundException, ServiceTaskNotFoundException;

    List<TaskDto> getWeekTasksByUserId(int userId, LocalDate clientCurrentDate, String sortType) throws ServiceNoSuchSortTypeException, ServiceUserNotFoundException, ServiceTaskNotFoundException;
}
