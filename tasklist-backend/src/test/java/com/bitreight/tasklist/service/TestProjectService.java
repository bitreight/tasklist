package com.bitreight.tasklist.service;

import com.bitreight.tasklist.dao.ProjectDao;
import com.bitreight.tasklist.dao.UserDao;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedProjectException;
import com.bitreight.tasklist.dto.ProjectDto;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.User;
import com.bitreight.tasklist.service.converter.ProjectDtoConverter;
import com.bitreight.tasklist.service.converter.impl.ProjectDtoConverterImpl;
import com.bitreight.tasklist.service.exception.ServiceProjectAlreadyExistsException;
import com.bitreight.tasklist.service.exception.ServiceProjectNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceUserNotFoundException;
import com.bitreight.tasklist.service.impl.ProjectServiceImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestProjectService {

    @Mock
    private UserDao mockUserDao;

    @Mock
    private ProjectDao mockProjectDao;

    @Spy
    private ProjectDtoConverter spyProjectConverter = new ProjectDtoConverterImpl();

    @InjectMocks
    private ProjectService projectService = new ProjectServiceImpl();

    private static User user = new User();

    private static ProjectDto projectDto = new ProjectDto();
    private static ProjectDto projectDtoToAdd = new ProjectDto();
    private static Project project = new Project();
    private static Project projectToSave = new Project();
    private static Project projectToUpdate = new Project();

    private static List<Project> projectsOfUser = new ArrayList<>(1);
    private static List<ProjectDto> projectDtosOfUser = new ArrayList<>(1);

    @BeforeClass
    public static void setUpBeforeClass() {
        user.setId(1);
        user.setUsername("test_user");
        user.setPassword("pass");

        projectDto.setId(1);
        projectDto.setTitle("test_project");
        projectDto.setDescription("test_desc");

        projectDtoToAdd.setTitle("new_project");
        projectDtoToAdd.setDescription("test_desc");

        project.setId(1);
        project.setTitle("test_project");
        project.setDescription("test_desc");
        project.setUser(user);

        projectToSave.setTitle("new_project");
        projectToSave.setDescription("test_desc");
        projectToSave.setUser(user);

        projectToUpdate.setId(1);
        projectToUpdate.setTitle("test_project");
        projectToUpdate.setDescription("test_desc");
        projectToUpdate.setUser(user);

        projectsOfUser.add(project);
        projectDtosOfUser.add(projectDto);
    }

    @Test
    public void testAddProject() throws ServiceProjectAlreadyExistsException,
            DaoSaveDuplicatedProjectException, ServiceUserNotFoundException {
        when(mockUserDao.findById(1)).thenReturn(user);

        projectService.add(projectDtoToAdd, 1);

        verify(spyProjectConverter).convertDto(projectDtoToAdd);
        verify(mockUserDao).findById(1);
        verify(mockProjectDao).save(projectToSave);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddProject_nullProjectDto() throws ServiceProjectAlreadyExistsException,
            DaoSaveDuplicatedProjectException, ServiceUserNotFoundException {
        projectService.add(null, 1);

        verify(spyProjectConverter, never()).convertDto(projectDtoToAdd);
        verify(mockUserDao, never()).findById(anyInt());
        verify(mockProjectDao, never()).save(any());
    }

    @Test(expected = ServiceUserNotFoundException.class)
    public void testAddProject_nonExistentUser() throws ServiceProjectAlreadyExistsException,
            DaoSaveDuplicatedProjectException, ServiceUserNotFoundException {
        when(mockUserDao.findById(1)).thenReturn(null);

        projectService.add(projectDtoToAdd, 1);

        verify(mockUserDao).findById(1);
        verify(spyProjectConverter,never()).convertDto(projectDtoToAdd);
        verify(mockProjectDao, never()).save(projectToSave);
    }

    @Test(expected = ServiceProjectAlreadyExistsException.class)
    public void testAddProject_duplicatedProject() throws ServiceProjectAlreadyExistsException,
            DaoSaveDuplicatedProjectException, ServiceUserNotFoundException {
        when(mockUserDao.findById(1)).thenReturn(user);
        doThrow(DaoSaveDuplicatedProjectException.class).when(mockProjectDao).save(projectToSave);

        projectService.add(projectDtoToAdd, 1);

        verify(spyProjectConverter).convertDto(projectDtoToAdd);
        verify(mockUserDao).findById(1);
        verify(mockProjectDao).save(projectToSave);
    }

    @Test
    public void testUpdateProject() throws ServiceProjectAlreadyExistsException,
            DaoSaveDuplicatedProjectException, ServiceProjectNotFoundException {
        when(mockProjectDao.findById(1)).thenReturn(project);

        projectService.update(projectDto);

        verify(spyProjectConverter).convertDto(projectDto);
        verify(mockProjectDao).update(projectToUpdate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateProject_nullProjectDto() throws ServiceProjectAlreadyExistsException,
            DaoSaveDuplicatedProjectException, ServiceProjectNotFoundException {
        projectService.update(null);

        verify(spyProjectConverter, never()).convertDto(projectDto);
        verify(mockProjectDao, never()).update(project);
    }

    @Test(expected = ServiceProjectNotFoundException.class)
    public void testUpdateProject_nonExistentProject() throws ServiceProjectAlreadyExistsException,
            DaoSaveDuplicatedProjectException, ServiceProjectNotFoundException {
        when(mockProjectDao.findById(1)).thenReturn(null);

        projectService.update(projectDto);

        verify(spyProjectConverter, never()).convertDto(projectDto);
        verify(mockProjectDao, never()).update(project);
    }

    @Test(expected = ServiceProjectAlreadyExistsException.class)
    public void testUpdateProject_duplicatedProject() throws ServiceProjectAlreadyExistsException,
            DaoSaveDuplicatedProjectException, ServiceProjectNotFoundException {
        when(mockProjectDao.findById(1)).thenReturn(project);
        doThrow(DaoSaveDuplicatedProjectException.class).when(mockProjectDao).update(projectToUpdate);

        projectService.update(projectDto);

        verify(spyProjectConverter).convertDto(projectDto);
        verify(mockProjectDao).update(projectToUpdate);
    }

    @Test
    public void testDeleteProjectById() throws ServiceProjectNotFoundException {
        when(mockProjectDao.findById(1)).thenReturn(project);
        projectService.deleteById(1);
        verify(mockProjectDao).delete(project);
    }

    @Test(expected = ServiceProjectNotFoundException.class)
    public void testDeleteProjectById_nonExistentProject() throws ServiceProjectNotFoundException {
        when(mockProjectDao.findById(1)).thenReturn(null);
        projectService.deleteById(1);
        verify(mockProjectDao, never()).delete(project);
    }

    @Test
    public void testGetProjectById() throws ServiceProjectNotFoundException {
        when(mockProjectDao.findById(1)).thenReturn(project);

        ProjectDto actualProjectDto = projectService.getById(1);

        assertEquals(actualProjectDto, projectDto);
        verify(spyProjectConverter).convertEntity(project);
        verify(mockProjectDao).findById(1);
    }

    @Test(expected = ServiceProjectNotFoundException.class)
    public void testGetProjectById_nonExistentProject() throws ServiceProjectNotFoundException {
        when(mockProjectDao.findById(1)).thenReturn(null);

        ProjectDto actualProjectDto = projectService.getById(1);

        verify(spyProjectConverter, never()).convertEntity(any());
        verify(mockProjectDao).findById(1);
    }

    @Test
    public void testGetProjectByUserId() throws ServiceUserNotFoundException,
            ServiceProjectNotFoundException {
        when(mockUserDao.findById(1)).thenReturn(user);
        when(mockProjectDao.findByUser(user)).thenReturn(projectsOfUser);

        List<ProjectDto> actualProjectDtosOfUser = projectService.getByUserId(1);

        assertEquals(actualProjectDtosOfUser, projectDtosOfUser);
        verify(mockUserDao).findById(1);
        verify(mockProjectDao).findByUser(user);
        verify(spyProjectConverter).convertEntities(projectsOfUser);
    }

    @Test(expected = ServiceUserNotFoundException.class)
    public void testGetProjectByUserId_nonExistentUser() throws ServiceUserNotFoundException,
            ServiceProjectNotFoundException {
        when(mockUserDao.findById(1)).thenReturn(null);

        List<ProjectDto> actualProjectDtosOfUser = projectService.getByUserId(1);

        verify(mockUserDao).findById(1);
        verify(mockProjectDao, never()).findByUser(user);
        verify(spyProjectConverter, never()).convertEntities(projectsOfUser);
    }

    @Test(expected = ServiceProjectNotFoundException.class)
    public void testGetProjectByUserId_noProjects() throws ServiceUserNotFoundException,
            ServiceProjectNotFoundException {
        when(mockUserDao.findById(1)).thenReturn(user);
        when(mockProjectDao.findByUser(user)).thenReturn(null);

        List<ProjectDto> actualProjectDtosOfUser = projectService.getByUserId(1);

        verify(mockUserDao).findById(1);
        verify(mockProjectDao).findByUser(user);
        verify(spyProjectConverter, never()).convertEntities(projectsOfUser);
    }
}
