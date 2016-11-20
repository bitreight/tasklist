package com.bitreight.tasklist.service;

import com.bitreight.tasklist.dto.TaskDto;
import com.bitreight.tasklist.service.exception.ServiceTaskAlreadyExistsException;
import com.bitreight.tasklist.service.exception.ServiceTaskVersionIsOutdatedException;

import java.util.List;

public interface TaskService {

    void add(TaskDto taskDto, int projectId) throws ServiceTaskAlreadyExistsException;

    void update(TaskDto taskDto) throws ServiceTaskVersionIsOutdatedException, ServiceTaskAlreadyExistsException;

    void deleteById(int taskId);

    TaskDto getById(int taskId);

    List<TaskDto> getByProjectId(int projectId);

    List<TaskDto> getAllTasksOfUser(int userId);

    void setIsCompleted(int taskId, boolean isCompleted);
}
