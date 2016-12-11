package com.bitreight.tasklist.service.impl;

import com.bitreight.tasklist.dao.ProjectDao;
import com.bitreight.tasklist.dao.TaskDao;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedTaskException;
import com.bitreight.tasklist.dao.exception.DaoUpdateNonActualVersionOfTaskException;
import com.bitreight.tasklist.dto.TaskDto;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.Task;
import com.bitreight.tasklist.service.TaskService;
import com.bitreight.tasklist.service.converter.TaskDtoConverter;
import com.bitreight.tasklist.service.exception.ServiceProjectNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceTaskAlreadyExistsException;
import com.bitreight.tasklist.service.exception.ServiceTaskNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceTaskVersionIsOutdatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("taskService")
@Transactional(rollbackFor = Exception.class)
public class TaskServiceImpl implements TaskService {

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
}