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
import com.bitreight.tasklist.util.date.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.next;
import static java.time.temporal.TemporalAdjusters.previous;

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

//        User userFromDb = userDao.findById(userId);
//        if(userFromDb == null) {
//            throw new ServiceUserNotFoundException("User not found.");
//        }
//
//        Date longAgo = new Date(Long.MIN_VALUE);
//        Date farFuture = new Date(Long.MAX_VALUE);
//
//        List<String> sortKeys = new ArrayList<>();
//        if(checkSortKey(primarySortKey)) {
//            sortKeys.add(primarySortKey.toLowerCase());
//        }
//        sortKeys.add(DEFAULT_SORT_KEY);
//
//        List<Task> tasksFromDb = taskFindDao.findByUserAndPeriod(userFromDb, longAgo, farFuture, sortKeys);
//        if(tasksFromDb == null) {
//            throw new ServiceTaskNotFoundException("Tasks not found.");
//        }

        return getByUserIdAndPeriod(userId, DateHelper.getMinLocalDate(), DateHelper.getMaxLocalDate(), primarySortKey);
    }

    @Override
    public List<TaskDto> getByProjectId(int projectId, String primarySortKey)
            throws ServiceProjectNotFoundException, ServiceTaskNotFoundException {

        Project projectFromDb = projectDao.findById(projectId);
        if(projectFromDb == null) {
            throw new ServiceProjectNotFoundException("Project not found.");
        }

//        Date longAgo = new Date(Long.MIN_VALUE);
//        Date farFuture = new Date(Long.MAX_VALUE);

        List<String> sortKeys = new ArrayList<>();
        if(checkSortKey(primarySortKey)) {
            sortKeys.add(primarySortKey.toLowerCase());
        }
        sortKeys.add(DEFAULT_SORT_KEY);

        List<Task> tasksFromDb = taskFindDao
                .findByProjectAndPeriod(projectFromDb, DateHelper.getMinLocalDate(), DateHelper.getMaxLocalDate(), sortKeys);
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

        LocalDate nextDay = clientCurrentDate.plusDays(1);

        return getByUserIdAndPeriod(userId, nextDay, nextDay, primarySortKey);
    }

    @Override
    public List<TaskDto> getWeekTasksByUserId(int userId, LocalDate clientCurrentDate, String primarySortKey)
            throws ServiceUserNotFoundException, ServiceTaskNotFoundException {

        if(clientCurrentDate == null) {
            throw new IllegalArgumentException("clientCurrentDate cannot be null.");
        }

        LocalDate monday = null;
        if(clientCurrentDate.getDayOfWeek() == DayOfWeek.MONDAY) {
            monday = clientCurrentDate;
        } else {
            monday = clientCurrentDate.with(previous(DayOfWeek.MONDAY));
        }

        LocalDate sunday = null;
        if(clientCurrentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            sunday = clientCurrentDate;
        } else {
            sunday = clientCurrentDate.with(next(DayOfWeek.SUNDAY));
        }

        return getByUserIdAndPeriod(userId, monday, sunday, primarySortKey);
    }

    private List<TaskDto> getByUserIdAndPeriod(int userId, LocalDate minDate, LocalDate maxDate, String primarySortKey)
            throws ServiceUserNotFoundException, ServiceTaskNotFoundException {

        User userFromDb = userDao.findById(userId);
        if(userFromDb == null) {
            throw new ServiceUserNotFoundException("User not found.");
        }

//        Date sqlMinDate = Date.valueOf(minDate);
//        Date sqlMaxDate = Date.valueOf(maxDate);

        List<String> sortKeys = new ArrayList<>();
        if(checkSortKey(primarySortKey)) {
            sortKeys.add(primarySortKey.toLowerCase());
        }
        sortKeys.add(DEFAULT_SORT_KEY);

        List<Task> tasksFromDb = taskFindDao.findByUserAndPeriod(userFromDb, minDate, maxDate, sortKeys);
        if(tasksFromDb == null) {
            throw new ServiceTaskNotFoundException("Tasks not found.");
        }

        return taskConverter.convertEntities(tasksFromDb);
    }

    private boolean checkSortKey(String sortKey) {
        try {
            SortKey.valueOf(sortKey.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
        return true;
    }
}
