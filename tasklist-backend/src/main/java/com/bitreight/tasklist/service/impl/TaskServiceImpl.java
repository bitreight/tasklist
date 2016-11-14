package com.bitreight.tasklist.service.impl;

import com.bitreight.tasklist.dao.ProjectDao;
import com.bitreight.tasklist.dao.TaskDao;
import com.bitreight.tasklist.dto.ProjectDto;
import com.bitreight.tasklist.dto.TaskDto;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.Task;
import com.bitreight.tasklist.service.TaskService;
import com.bitreight.tasklist.service.converter.TaskDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private TaskDtoConverter taskConverter;

    @Override
    public void add(TaskDto taskDto, int projectId) {
        if(taskDto != null && projectId > 0) {
            Project project = projectDao.findById(projectId);

            if(project != null) {
                Task task = taskConverter.convertDto(taskDto);
                task.setId(0);
                task.setProject(project);
                taskDao.save(task);
            }
        }
    }

    @Override
    public void update(TaskDto taskDto) {
        if(taskDto != null) {
            Task task = taskConverter.convertDto(taskDto);
            taskDao.update(task);
        }
    }

    @Override
    public void deleteById(int taskId) {
        if(taskId > 0) {
            taskDao.deleteById(taskId);
        }
    }

    @Override
    public TaskDto getById(int taskId) {
        TaskDto taskDto = null;

        if(taskId > 0) {
            Task task = taskDao.findById(taskId);

            if(task != null) {
                taskDto = taskConverter.convertEntity(task);
            }
        }

        return taskDto;
    }

    @Override
    public List<TaskDto> getByProjectId(int projectId) {
        List<TaskDto> taskDtos = null;

        if(projectId > 0) {
            Project project = projectDao.findById(projectId);

            if(project != null) {
                List<Task> tasks = taskDao.findByProject(project);

                if(tasks != null) {
                    taskDtos = taskConverter.convertEntities(tasks);
                }
            }
        }

        return taskDtos;
    }

    //not implemented in dao
    @Override
    public List<TaskDto> getAllTasksOfUser(int userId) {
        return null;
    }

    @Override
    public void setIsCompleted(int taskId, boolean isCompleted) {
        if(taskId > 0) {
            taskDao.setIsCompleted(taskId, isCompleted);
        }
    }
}
