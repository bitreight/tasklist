package com.bitreight.tasklist.dao;

import com.bitreight.tasklist.config.DaoContextConfiguration;
import com.bitreight.tasklist.config.JpaConfiguration;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JpaConfiguration.class, DaoContextConfiguration.class },
        loader = AnnotationConfigContextLoader.class)
@Transactional
public class TestProjectDao {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private UserDao userDao;

    private User user;

    private List<Project> projects;

    @Before
    public void setUp() {
        user = new User();
        user.setUsername("username");
        user.setPassword("pass");
        userDao.save(user);

        projects = new ArrayList<>();

        Project project1 = new Project();
        project1.setTitle("project_one");
        project1.setDescription("description_one");
        project1.setUser(user);
        projects.add(project1);

        Project project2 = new Project();
        project2.setTitle("project_two");
        project2.setDescription("description_two");
        project2.setUser(user);
        projects.add(project2);

        projects.forEach(project -> projectDao.save(project));
    }

    @After
    public void tearDown() {
        projects.forEach(project -> projectDao.deleteById(project.getId()));
        userDao.deleteById(user.getId());
    }

    @Test
    public void testFindProjectById() {
        Project projectFromDb = projectDao.findById(projects.get(0).getId());
        assertEquals(projects.get(0), projectFromDb);
    }

    @Test
    public void testFindProjectByUser() {
        List<Project> projectsFromDb = projectDao.findByUser(user);
        assertEquals(projects, projectsFromDb);
    }

    @Test
    public void testUpdateProject() {
        for(Project project : projects) {
            project.setTitle("test" + projects.indexOf(project));
            project.setDescription("test_description");
            projectDao.update(project);
        }

        List<Project> projectsFromDb = projectDao.findByUser(user);

        assertEquals(projects, projectsFromDb);
    }
}