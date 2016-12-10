package com.bitreight.tasklist.dao;

import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedTaskException;
import com.bitreight.tasklist.dao.exception.DaoUpdateNonActualVersionOfTaskException;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.Task;
import com.bitreight.tasklist.entity.User;
import org.hibernate.Criteria;

import java.sql.Date;
import java.util.List;

public interface TaskDao {

    void save(Task task) throws DaoSaveDuplicatedTaskException;

    void update(Task task) throws DaoUpdateNonActualVersionOfTaskException, DaoSaveDuplicatedTaskException;

    void delete(Task task);

    Task findById(int taskId);

    List<Task> findByUserAndMaxDeadline(User user, Date date, String sortField);

    List<Task> findByProjectAndMaxDeadline(Project project, Date date, String sortField);
}