package com.bitreight.tasklist.service.impl;

import com.bitreight.tasklist.dao.ProjectDao;
import com.bitreight.tasklist.dao.TaskDao;
import com.bitreight.tasklist.dao.UserDao;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedTaskException;
import com.bitreight.tasklist.dao.exception.DaoUpdateNonActualVersionOfTaskException;
import com.bitreight.tasklist.dto.TaskDto;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.SortKey;
import com.bitreight.tasklist.entity.Task;
import com.bitreight.tasklist.entity.User;
import com.bitreight.tasklist.service.TaskService;
import com.bitreight.tasklist.service.converter.TaskDtoConverter;
import com.bitreight.tasklist.service.exception.ServiceNoSuchSortTypeException;
import com.bitreight.tasklist.service.exception.ServiceProjectNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceTaskAlreadyExistsException;
import com.bitreight.tasklist.service.exception.ServiceTaskNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceTaskVersionIsOutdatedException;
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

@Service("taskService")
@Transactional(rollbackFor = Exception.class)
public class TaskServiceImpl implements TaskService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private TaskDtoConverter taskConverter;

    @Override
    public int add(TaskDto taskDto, int projectId) throws ServiceTaskAlreadyExistsException,
            ServiceProjectNotFoundException {

        if(taskDto == null) {
            throw new IllegalArgumentException("taskDto cannot be null");
        }

        Project projectFromDb = projectDao.findById(projectId);
        if(projectFromDb == null) {
            throw new ServiceProjectNotFoundException("Project not found.");
        }

        Task taskToSave = taskConverter.convertDto(taskDto);
        taskToSave.setId(0);
        taskToSave.setProject(projectFromDb);

        try {
            taskDao.save(taskToSave);
        } catch (DaoSaveDuplicatedTaskException e) {
            throw new ServiceTaskAlreadyExistsException("Task already exists.", e);
        }

        return taskToSave.getId();
    }

    @Override
    public void update(TaskDto taskDto) throws ServiceTaskVersionIsOutdatedException,
            ServiceTaskAlreadyExistsException, ServiceTaskNotFoundException {

        if(taskDto == null) {
            throw new IllegalArgumentException("taskDto cannot be null.");
        }

        Task taskFromDb = taskDao.findById(taskDto.getId());
        if(taskFromDb == null) {
            throw new ServiceTaskNotFoundException("Task not found.");
        }

        Task taskToUpdate = taskConverter.convertDto(taskDto);
        taskToUpdate.setProject(taskFromDb.getProject());

        try {
            taskDao.update(taskToUpdate);
        } catch (DaoUpdateNonActualVersionOfTaskException e) {
            throw new ServiceTaskVersionIsOutdatedException("Can't update task. Version is outdated.", e);

        } catch (DaoSaveDuplicatedTaskException e) {
            throw new ServiceTaskAlreadyExistsException("Task already exists.", e);
        }
    }

    @Override
    public void deleteById(int taskId) throws ServiceTaskNotFoundException {
        Task taskToDelete = taskDao.findById(taskId);
        if(taskToDelete == null) {
            throw new ServiceTaskNotFoundException("Task not found.");
        }
        taskDao.delete(taskToDelete);
    }

    @Override
    public TaskDto getById(int taskId) throws ServiceTaskNotFoundException {
        Task taskFromDb = taskDao.findById(taskId);
        if(taskFromDb == null) {
            throw new ServiceTaskNotFoundException("Task not found.");
        }
        return taskConverter.convertEntity(taskFromDb);
    }

    @Override
    public void setIsCompleted(int taskId, boolean isCompleted) throws ServiceTaskNotFoundException {
        Task taskFromDb = taskDao.findById(taskId);
        if(taskFromDb == null) {
            throw new ServiceTaskNotFoundException("Task not found.");
        }
        taskFromDb.setCompleted(isCompleted);
    }


    @Override
    public List<TaskDto> getByProjectId(int projectId, String primarySortType) throws ServiceProjectNotFoundException,
            ServiceNoSuchSortTypeException, ServiceTaskNotFoundException {

        Project projectFromDb = projectDao.findById(projectId);
        if(projectFromDb == null) {
            throw new ServiceProjectNotFoundException("Project not found.");
        }

        List<String> sortTypes = new ArrayList<>();
        if(checkSortType(primarySortType)) {
            sortTypes.add(primarySortType);
        }

        Date farFuture = new Date(Long.MAX_VALUE);

        List<Task> tasksFromDb = taskDao.findByProjectAndMaxDeadline(projectFromDb, farFuture, sortType);
        if(tasksFromDb == null) {
            throw new ServiceTaskNotFoundException("Tasks not found.");
        }

        return taskConverter.convertEntities(tasksFromDb);
    }

    @Override
    public List<TaskDto> getTodayTasksByUserId(int userId, LocalDate clientCurrentDate, String sortType)
            throws ServiceNoSuchSortTypeException, ServiceUserNotFoundException, ServiceTaskNotFoundException {

        if(clientCurrentDate == null) {
            throw new IllegalArgumentException("clientCurrentDate cannot be null.");
        }

        return getByUserIdAndMaxDeadline(userId, clientCurrentDate, sortType);
    }

    @Override
    public List<TaskDto> getWeekTasksByUserId(int userId, LocalDate clientCurrentDate, String sortType)
            throws ServiceNoSuchSortTypeException, ServiceUserNotFoundException, ServiceTaskNotFoundException {

        if(clientCurrentDate == null) {
            throw new IllegalArgumentException("clientCurrentDate cannot be null.");
        }

        LocalDate maxDeadline = null;
        if(clientCurrentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            maxDeadline = clientCurrentDate;
        } else {
            maxDeadline = clientCurrentDate.with(next(DayOfWeek.SUNDAY));
        }

        return getByUserIdAndMaxDeadline(userId, maxDeadline, sortType);
    }

    private List<TaskDto> getByUserIdAndMaxDeadline(int userId, LocalDate maxDeadline, String sortType)
            throws ServiceUserNotFoundException, ServiceNoSuchSortTypeException, ServiceTaskNotFoundException {

        User userFromDb = userDao.findById(userId);
        if(userFromDb == null) {
            throw new ServiceUserNotFoundException("User not found.");
        }

        Date sqlMaxDeadline = Date.valueOf(maxDeadline);
        checkSortType(sortType);

        List<Task> tasksFromDb = taskDao.findByUserAndMaxDeadline(userFromDb, sqlMaxDeadline, sortType);
        if(tasksFromDb == null) {
            throw new ServiceTaskNotFoundException("Tasks not found.");
        }

        return taskConverter.convertEntities(tasksFromDb);
    }

    private boolean checkSortType(String sortType) throws ServiceNoSuchSortTypeException {
        try {
            SortKey.valueOf(sortType);
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
        return true;
    }

    private String getDefaultSortKey() {
        return SortKey.TITLE.toString().toLowerCase();
    }
}
