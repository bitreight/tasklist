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

        projectsOfUser.add(project);
        projectDtosOfUser.add(projectDto);
    }

    @Test
    public void testAddProject() throws ServiceProjectAlreadyExistsException,
            DaoSaveDuplicatedProjectException {
        when(mockUserDao.findById(1)).thenReturn(user);

        projectService.add(projectDtoToAdd, 1);

        verify(spyProjectConverter).convertDto(projectDtoToAdd);
        verify(mockUserDao).findById(1);
        verify(mockProjectDao).save(projectToSave);
    }

    @Test
    public void testAddProject_nullProjectDtoAndZeroUserId() throws ServiceProjectAlreadyExistsException,
            DaoSaveDuplicatedProjectException {
        projectService.add(null, 0);

        verify(spyProjectConverter, never()).convertDto(projectDtoToAdd);
        verify(mockUserDao, never()).findById(anyInt());
        verify(mockProjectDao, never()).save(any());
    }

    @Test(expected = ServiceProjectAlreadyExistsException.class)
    public void testAddProject_duplicatedProject() throws ServiceProjectAlreadyExistsException,
            DaoSaveDuplicatedProjectException {
        when(mockUserDao.findById(1)).thenReturn(user);
        doThrow(DaoSaveDuplicatedProjectException.class).when(mockProjectDao).save(projectToSave);

        projectService.add(projectDtoToAdd, 1);

        verify(spyProjectConverter).convertDto(projectDtoToAdd);
        verify(mockUserDao).findById(1);
        verify(mockProjectDao).save(projectToSave);
    }

    @Test
    public void testAddProject_nonExistentUser() throws ServiceProjectAlreadyExistsException,
            DaoSaveDuplicatedProjectException {
        when(mockUserDao.findById(1)).thenReturn(null);

        projectService.add(projectDtoToAdd, 1);

        verify(mockUserDao).findById(1);
        verify(spyProjectConverter,never()).convertDto(projectDtoToAdd);
        verify(mockProjectDao, never()).save(projectToSave);
    }

    @Test
    public void testUpdateProject() throws ServiceProjectAlreadyExistsException,
            DaoSaveDuplicatedProjectException {
        projectService.update(projectDto);

        verify(spyProjectConverter).convertDto(projectDto);
        verify(mockProjectDao).update(projectToUpdate);
    }

    @Test
    public void testUpdateProject_nullProjectDto() throws ServiceProjectAlreadyExistsException,
            DaoSaveDuplicatedProjectException {
        projectService.update(null);

        verify(spyProjectConverter, never()).convertDto(projectDto);
        verify(mockProjectDao, never()).update(project);
    }

    @Test(expected = ServiceProjectAlreadyExistsException.class)
    public void testUpdateProject_duplicatedProject() throws ServiceProjectAlreadyExistsException,
            DaoSaveDuplicatedProjectException {
        doThrow(DaoSaveDuplicatedProjectException.class).when(mockProjectDao).update(projectToUpdate);

        projectService.update(projectDto);

        verify(spyProjectConverter).convertDto(projectDto);
        verify(mockProjectDao).update(projectToUpdate);
    }

    @Test
    public void testDeleteProjectById() {
        projectService.deleteById(1);
        verify(mockProjectDao).deleteById(1);
    }

    @Test
    public void testDeleteProjectById_zeroProjectId() {
        projectService.deleteById(0);
        verify(mockProjectDao, never()).deleteById(1);
    }

    @Test
    public void testGetProjectById() {
        when(mockProjectDao.findById(1)).thenReturn(project);

        ProjectDto actualProjectDto = projectService.getById(1);

        assertEquals(actualProjectDto, projectDto);
        verify(spyProjectConverter).convertEntity(project);
        verify(mockProjectDao).findById(1);
    }

    @Test
    public void testGetProjectById_zeroProjectId() {
        ProjectDto actualProjectDto = projectService.getById(0);

        assertNull(actualProjectDto);
        verify(spyProjectConverter, never()).convertEntity(any());
        verify(mockProjectDao, never()).findById(anyInt());
    }

    @Test
    public void testGetProjectById_nonExistentProject() {
        when(mockProjectDao.findById(1)).thenReturn(null);

        ProjectDto actualProjectDto = projectService.getById(1);

        assertNull(actualProjectDto);
        verify(spyProjectConverter, never()).convertEntity(any());
        verify(mockProjectDao).findById(1);
    }

    @Test
    public void testGetProjectByUserId() {
        when(mockUserDao.findById(1)).thenReturn(user);
        when(mockProjectDao.findByUser(user)).thenReturn(projectsOfUser);

        List<ProjectDto> actualProjectDtosOfUser = projectService.getByUserId(1);

        assertEquals(actualProjectDtosOfUser, projectDtosOfUser);
        verify(mockUserDao).findById(1);
        verify(mockProjectDao).findByUser(user);
        verify(spyProjectConverter).convertEntities(projectsOfUser);
    }

    @Test
    public void testGetProjectByUserId_zeroUserId() {
        List<ProjectDto> actualProjectDtosOfUser = projectService.getByUserId(0);

        assertNull(actualProjectDtosOfUser);
        verify(mockUserDao, never()).findById(1);
        verify(mockProjectDao, never()).findByUser(user);
        verify(spyProjectConverter, never()).convertEntities(projectsOfUser);
    }

    @Test
    public void testGetProjectByUserId_nonExistentUser() {
        when(mockUserDao.findById(1)).thenReturn(null);

        List<ProjectDto> actualProjectDtosOfUser = projectService.getByUserId(1);

        assertNull(actualProjectDtosOfUser);
        verify(mockUserDao).findById(1);
        verify(mockProjectDao, never()).findByUser(user);
        verify(spyProjectConverter, never()).convertEntities(projectsOfUser);
    }

    @Test
    public void testGetProjectByUserId_noProjects() {
        when(mockUserDao.findById(1)).thenReturn(user);
        when(mockProjectDao.findByUser(user)).thenReturn(null);

        List<ProjectDto> actualProjectDtosOfUser = projectService.getByUserId(1);

        assertNull(actualProjectDtosOfUser);
        verify(mockUserDao).findById(1);
        verify(mockProjectDao).findByUser(user);
        verify(spyProjectConverter, never()).convertEntities(projectsOfUser);
    }
}
