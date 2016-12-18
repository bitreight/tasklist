package com.bitreight.tasklist.dao;

import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedTaskException;
import com.bitreight.tasklist.dao.exception.DaoUpdateNonActualVersionOfTaskException;
import com.bitreight.tasklist.entity.Task;

public interface TaskDao {

    void save(Task task) throws DaoSaveDuplicatedTaskException;

    void update(Task task) throws DaoUpdateNonActualVersionOfTaskException, DaoSaveDuplicatedTaskException;

    void delete(Task task);

    Task findById(int taskId);
}