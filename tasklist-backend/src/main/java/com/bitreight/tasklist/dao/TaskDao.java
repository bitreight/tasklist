package com.bitreight.tasklist.dao;

import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.Task;

import java.util.List;

public interface TaskDao {
    void save(Task task);

    void update(Task task);

    void deleteById(int taskId);

    Task findById(int taskId);

    List<Task> findByProject(Project project);

    void setIsCompleted(int taskId, boolean isCompleted);
}
