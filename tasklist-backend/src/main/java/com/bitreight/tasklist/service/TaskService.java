package com.bitreight.tasklist.service;

import com.bitreight.tasklist.dto.TaskDto;
import com.bitreight.tasklist.service.exception.ServiceProjectNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceTaskAlreadyExistsException;
import com.bitreight.tasklist.service.exception.ServiceTaskNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceTaskVersionIsOutdatedException;

public interface TaskService {

    int add(TaskDto taskDto, int projectId) throws ServiceTaskAlreadyExistsException, ServiceProjectNotFoundException;

    void update(TaskDto taskDto) throws ServiceTaskVersionIsOutdatedException, ServiceTaskAlreadyExistsException, ServiceTaskNotFoundException;

    void deleteById(int taskId) throws ServiceTaskNotFoundException;

    TaskDto getById(int taskId) throws ServiceTaskNotFoundException;

    void setIsCompleted(int taskId, boolean isCompleted) throws ServiceTaskNotFoundException;
}
