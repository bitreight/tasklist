package com.bitreight.tasklist.converter;

import com.bitreight.tasklist.dto.ProjectDto;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.service.converter.ProjectDtoConverter;
import com.bitreight.tasklist.service.converter.impl.ProjectDtoConverterImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestProjectDtoConverter {

    private ProjectDtoConverter projectConverter = new ProjectDtoConverterImpl();

    private static ProjectDto projectDto = new ProjectDto();
    private static Project project = new Project();

    private static List<Project> projects = new ArrayList<>(1);
    private static List<ProjectDto> projectDtos = new ArrayList<>(1);

    @BeforeClass
    public static void setUpBeforeClass() {
        projectDto.setId(1);
        projectDto.setTitle("test_project");
        projectDto.setDescription("test_desc");

        project.setId(1);
        project.setTitle("test_project");
        project.setDescription("test_desc");

        projects.add(project);
        projectDtos.add(projectDto);
    }

    @Test
    public void testConvertProjectDto() {
        Project actualProject = projectConverter.convertDto(projectDto);
        assertEquals(actualProject, project);
    }

    @Test
    public void testConvertProjectEntity() {
        ProjectDto actualProjectDto = projectConverter.convertEntity(project);
        assertEquals(actualProjectDto, projectDto);
    }

    @Test
    public void testConvertProjectDtos() {
        List<Project> actualProjects = projectConverter.convertDtos(projectDtos);
        assertEquals(actualProjects, projects);
    }

    @Test
    public void testConvertProjectEntities() {
        List<ProjectDto> actualProjectDtos = projectConverter.convertEntities(projects);
        assertEquals(actualProjectDtos, projectDtos);
    }

    @Test
    public void testConvertProjectDtos_emptyList() {
        List<Project> actualProjects = projectConverter.convertDtos(new ArrayList<>(0));
        assertTrue(actualProjects.isEmpty());
    }

    @Test
    public void testConvertProjectEntities_emptyList() {
        List<ProjectDto> actualProjectDtos = projectConverter.convertEntities(new ArrayList<>(0));
        assertTrue(actualProjectDtos.isEmpty());
    }
}
