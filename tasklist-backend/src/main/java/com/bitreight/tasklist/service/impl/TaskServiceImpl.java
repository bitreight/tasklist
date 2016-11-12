package com.bitreight.tasklist.service.impl;

import com.bitreight.tasklist.dto.TaskDto;
import com.bitreight.tasklist.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {
    @Override
    public void add(TaskDto taskDto, int projectId) {

    }

    @Override
    public void update(TaskDto taskDto) {

    }

    @Override
    public void deleteById(int taskId) {

    }

    @Override
    public TaskDto getById(int taskId) {
        return null;
    }

    @Override
    public List<TaskDto> getByProjectId(int projectId) {
        return null;
    }

    @Override
    public List<TaskDto> getAllTasksOfUser(int userId) {
        return null;
    }

    @Override
    public void setCompleted(int taskId) {

    }
}
