package com.bitreight.tasklist.dao;

import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedTaskException;
import com.bitreight.tasklist.dao.exception.DaoUpdateNonActualVersionOfTaskException;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.Task;

import java.util.List;

public interface TaskDao {
    void save(Task task) throws DaoSaveDuplicatedTaskException;

    void update(Task task) throws DaoUpdateNonActualVersionOfTaskException;

    void deleteById(int taskId);

    Task findById(int taskId);

    List<Task> findByProject(Project project);

    void setIsCompleted(int taskId, boolean isCompleted);
}
