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
    public void add(ProjectDto projectDto, int userId) throws ServiceProjectAlreadyExistsException {
        if(projectDto != null && userId > 0) {
            User user = userDao.findById(userId);
            try {
                if (user != null) {
                    Project project = projectConverter.convertDto(projectDto);
                    project.setId(0);
                    project.setUser(user);
                    projectDao.save(project);
                }

            } catch (DaoSaveDuplicatedProjectException e) {
                throw new ServiceProjectAlreadyExistsException("Can't create project. Already exists.", e);
            }
        }
    }

    @Override
    public void update(ProjectDto projectDto) throws ServiceProjectAlreadyExistsException {
        if(projectDto != null) {
            Project project = projectConverter.convertDto(projectDto);
            try {
                projectDao.update(project);

            } catch (DaoSaveDuplicatedProjectException e) {
                throw new ServiceProjectAlreadyExistsException("Can't update project. Already exists.", e);
            }
        }
    }

    @Override
    public void deleteById(int projectId) {
        if(projectId > 0) {
            projectDao.deleteById(projectId);
        }
    }

    @Override
    public ProjectDto getById(int projectId) {
        ProjectDto projectDto = null;

        if(projectId > 0) {
            Project project = projectDao.findById(projectId);

            if(project != null) {
                projectDto = projectConverter.convertEntity(project);
            }
        }

        return projectDto;
    }

    @Override
    public List<ProjectDto> getByUserId(int userId) {
        List<ProjectDto> projectDtos = null;

        if(userId > 0) {
            User user = userDao.findById(userId);

            if(user != null) {
                List<Project> projects = projectDao.findByUser(user);

                if(projects != null) {
                    projectDtos = projectConverter.convertEntities(projects);
                }
            }
        }

        return projectDtos;
    }
}
