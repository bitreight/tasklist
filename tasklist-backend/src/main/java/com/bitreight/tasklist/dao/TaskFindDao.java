package com.bitreight.tasklist.dao;

import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.Task;
import com.bitreight.tasklist.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface TaskFindDao {

    List<Task> findByUserAndPeriod(User user, LocalDate minDate, LocalDate maxDate, List<String> orderFields);

    List<Task> findByProjectAndPeriod(Project project, LocalDate minDate, LocalDate maxDate, List<String> orderFields);
}
