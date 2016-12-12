package com.bitreight.tasklist.service.impl;

import com.bitreight.tasklist.dao.ProjectDao;
import com.bitreight.tasklist.dao.TaskFindDao;
import com.bitreight.tasklist.dao.UserDao;
import com.bitreight.tasklist.dto.TaskDto;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.SortKey;
import com.bitreight.tasklist.entity.Task;
import com.bitreight.tasklist.entity.User;
import com.bitreight.tasklist.service.TaskSearchService;
import com.bitreight.tasklist.service.converter.TaskDtoConverter;
import com.bitreight.tasklist.service.exception.ServiceProjectNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceTaskNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceUserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.next;

@Service("taskSearchService")
@Transactional(rollbackFor = Exception.class)
public class TaskSearchServiceImpl implements TaskSearchService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private TaskFindDao taskFindDao;

    @Autowired
    private TaskDtoConverter taskConverter;

    private static final String DEFAULT_SORT_KEY = SortKey.TITLE.toString().toLowerCase();

    @Override
    public List<TaskDto> getByUserId(int userId, String primarySortKey)
            throws ServiceUserNotFoundException, ServiceTaskNotFoundException {

        User userFromDb = userDao.findById(userId);
        if(userFromDb == null) {
            throw new ServiceUserNotFoundException("User not found.");
        }

        Date farFuture = new Date(Long.MAX_VALUE);

        List<String> sortKeys = new ArrayList<>();
        if(checkSortKey(primarySortKey)) {
            sortKeys.add(primarySortKey);
        }
        sortKeys.add(DEFAULT_SORT_KEY);

        List<Task> tasksFromDb = taskFindDao.findByUserAndMaxDeadline(userFromDb, farFuture, sortKeys);
        if(tasksFromDb == null) {
            throw new ServiceTaskNotFoundException("Tasks not found.");
        }

        return taskConverter.convertEntities(tasksFromDb);
    }

    @Override
    public List<TaskDto> getByProjectId(int projectId, String primarySortKey)
            throws ServiceProjectNotFoundException, ServiceTaskNotFoundException {

        Project projectFromDb = projectDao.findById(projectId);
        if(projectFromDb == null) {
            throw new ServiceProjectNotFoundException("Project not found.");
        }

        Date farFuture = new Date(Long.MAX_VALUE);

        List<String> sortKeys = new ArrayList<>();
        if(checkSortKey(primarySortKey)) {
            sortKeys.add(primarySortKey);
        }
        sortKeys.add(DEFAULT_SORT_KEY);

        List<Task> tasksFromDb = taskFindDao.findByProjectAndMaxDeadline(projectFromDb, farFuture, sortKeys);
        if(tasksFromDb == null) {
            throw new ServiceTaskNotFoundException("Tasks not found.");
        }

        return taskConverter.convertEntities(tasksFromDb);
    }

    @Override
    public List<TaskDto> getTodayTasksByUserId(int userId, LocalDate clientCurrentDate, String primarySortKey)
            throws ServiceUserNotFoundException, ServiceTaskNotFoundException {

        if(clientCurrentDate == null) {
            throw new IllegalArgumentException("clientCurrentDate cannot be null.");
        }

        return getByUserIdAndMaxDeadline(userId, clientCurrentDate, primarySortKey);
    }

    @Override
    public List<TaskDto> getWeekTasksByUserId(int userId, LocalDate clientCurrentDate, String primarySortKey)
            throws ServiceUserNotFoundException, ServiceTaskNotFoundException {

        if(clientCurrentDate == null) {
            throw new IllegalArgumentException("clientCurrentDate cannot be null.");
        }

        LocalDate maxDeadline = null;
        if(clientCurrentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            maxDeadline = clientCurrentDate;
        } else {
            maxDeadline = clientCurrentDate.with(next(DayOfWeek.SUNDAY));
        }

        return getByUserIdAndMaxDeadline(userId, maxDeadline, primarySortKey);
    }

    private List<TaskDto> getByUserIdAndMaxDeadline(int userId, LocalDate maxDeadline, String primarySortKey)
            throws ServiceUserNotFoundException, ServiceTaskNotFoundException {

        User userFromDb = userDao.findById(userId);
        if(userFromDb == null) {
            throw new ServiceUserNotFoundException("User not found.");
        }

        Date sqlMaxDeadline = Date.valueOf(maxDeadline);

        List<String> sortKeys = new ArrayList<>();
        if(checkSortKey(primarySortKey)) {
            sortKeys.add(primarySortKey);
        }
        sortKeys.add(DEFAULT_SORT_KEY);

        List<Task> tasksFromDb = taskFindDao.findByUserAndMaxDeadline(userFromDb, sqlMaxDeadline, sortKeys);
        if(tasksFromDb == null) {
            throw new ServiceTaskNotFoundException("Tasks not found.");
        }

        return taskConverter.convertEntities(tasksFromDb);
    }

    private boolean checkSortKey(String sortKey) {
        try {
            SortKey.valueOf(sortKey);
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
        return true;
    }
}
