package com.bitreight.tasklist.service;

import com.bitreight.tasklist.dto.TaskDto;

import java.util.List;

public interface TaskService {

    void add(TaskDto taskDto, int projectId);

    void update(TaskDto taskDto);

    void deleteById(int taskId);

    TaskDto getById(int taskId);

    List<TaskDto> getByProjectId(int projectId);

    List<TaskDto> getAllTasksOfUser(int userId);

    void setCompleted(int taskId);
}
