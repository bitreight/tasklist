package com.bitreight.tasklist.dao;

import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.Task;
import com.bitreight.tasklist.entity.User;

import java.sql.Date;
import java.util.List;

public interface TaskFindDao {

    List<Task> findByUserAndMaxDeadline(User user, Date deadline, List<String> orderFields);

    List<Task> findByProjectAndMaxDeadline(Project project, Date deadline, List<String> orderFields);
}
