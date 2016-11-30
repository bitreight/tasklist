package com.bitreight.tasklist.service.impl;

import com.bitreight.tasklist.dao.ProjectDao;
import com.bitreight.tasklist.dao.UserDao;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedProjectException;
import com.bitreight.tasklist.dto.ProjectDto;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.User;
import com.bitreight.tasklist.service.ProjectService;
import com.bitreight.tasklist.service.converter.ProjectDtoConverter;
import com.bitreight.tasklist.service.exception.ServiceProjectAlreadyExistsException;
import com.bitreight.tasklist.service.exception.ServiceProjectCreationException;
import com.bitreight.tasklist.service.exception.ServiceProjectNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceUserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("projectService")
@Transactional(rollbackFor = Exception.class)
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ProjectDtoConverter projectConverter;

    @Override
    public int add(ProjectDto projectDto, int userId) throws ServiceProjectAlreadyExistsException,
            ServiceUserNotFoundException {

        if(projectDto == null) {
            throw new IllegalArgumentException("projectDto cannot be null");
        }

        User user = userDao.findById(userId);
        if(user == null) {
            throw new ServiceUserNotFoundException("User not found.");
        }

        Project project = projectConverter.convertDto(projectDto);
        project.setId(0);
        project.setUser(user);

        try {
            projectDao.save(project);
        } catch (DaoSaveDuplicatedProjectException e) {
            throw new ServiceProjectAlreadyExistsException("Project already exists.", e);
        }

        return project.getId();
    }

    @Override
    public void update(ProjectDto projectDto) throws ServiceProjectAlreadyExistsException,
            ServiceProjectNotFoundException {

        if(projectDto == null) {
            throw new IllegalArgumentException("projectDto cannot be null.");
        }

        Project projectFromDb = projectDao.findById(projectDto.getId());
        if(projectFromDb == null) {
            throw new ServiceProjectNotFoundException("Project not found.");
        }

        Project project = projectConverter.convertDto(projectDto);

        try {
            projectDao.update(project);
        } catch (DaoSaveDuplicatedProjectException e) {
            throw new ServiceProjectAlreadyExistsException("Project already exists.", e);
        }
    }

    @Override
    public void deleteById(int projectId) throws ServiceProjectNotFoundException {
        Project project = projectDao.findById(projectId);
        if(project == null) {
            throw new ServiceProjectNotFoundException("Project not found.");
        }
        projectDao.delete(project);
    }

    @Override
    public ProjectDto getById(int projectId) throws ServiceProjectNotFoundException {
        Project project = projectDao.findById(projectId);
        if(project == null) {
            throw new ServiceProjectNotFoundException("Project not found.");
        }
        return projectConverter.convertEntity(project);
    }

    @Override
    public List<ProjectDto> getByUserId(int userId) throws ServiceUserNotFoundException,
            ServiceProjectNotFoundException {

        User user = userDao.findById(userId);
        if (user == null) {
            throw new ServiceUserNotFoundException("User not found.");
        }

        List<Project> projects = projectDao.findByUser(user);
        if (projects == null) {
            throw new ServiceProjectNotFoundException("Projects not found.");
        }

        return projectConverter.convertEntities(projects);
    }
}
