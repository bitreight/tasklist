package com.bitreight.tasklist.dao;

import com.bitreight.tasklist.config.BackendConfiguration;
import com.bitreight.tasklist.config.DaoContextConfiguration;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedProjectException;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedTaskException;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedUserException;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.Task;
import com.bitreight.tasklist.entity.TaskPriority;
import com.bitreight.tasklist.entity.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BackendConfiguration.class, DaoContextConfiguration.class },
        loader = AnnotationConfigContextLoader.class)
@Transactional
@ActiveProfiles("dev")
public class TestUserDao {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private TaskDao taskDao;

    private User user;
    private Project project;
    private Task task;

    @Before
    public void setUp() throws DaoSaveDuplicatedUserException,
            DaoSaveDuplicatedProjectException, DaoSaveDuplicatedTaskException {

        user = new User();
        user.setUsername("username");
        user.setPassword("pass");
        user.setName("name");
        user.setSurname("surname");
        userDao.save(user);

        project = new Project();
        project.setTitle("project");
        project.setDescription("description");
        project.setUser(user);
        projectDao.save(project);

        task = new Task();
        task.setTitle("task");
        task.setPriority(TaskPriority.NORMAL);
        task.setProject(project);
        taskDao.save(task);
    }

    @After
    public void tearDown() {
        userDao.delete(user);
    }

    @Test(expected = DaoSaveDuplicatedUserException.class)
    public void testSaveUser_withDuplicatedUsername() throws DaoSaveDuplicatedUserException {
        User duplicatedUser = new User();
        duplicatedUser.setUsername(user.getUsername());
        duplicatedUser.setPassword(user.getPassword());
        userDao.save(duplicatedUser);
    }

    @Test
    public void testFindUserById() {
        User userFromDb = userDao.findById(user.getId());
        assertEquals(user, userFromDb);
    }

    @Test
    public void testFindUserById_invalidId() {
        User userFromDb = userDao.findById(-1);
        assertNull(userFromDb);
    }

    @Test
    public void testFindUserByUsername() {
        User userFromDb = userDao.findByUsername(user.getUsername());
        assertEquals(user, userFromDb);
    }

    @Test
    public void testFindUserByUsernameAndPassword_nonExistentUsername() {
        User userFromDb = userDao.findByUsername("");
        assertNull(userFromDb);
    }

    @Test
    public void testUpdateUser() {
        user.setUsername("test");
        user.setPassword("test");
        user.setName("test");
        user.setSurname("test");

        userDao.update(user);
        User userFromDb = userDao.findById(user.getId());

        assertEquals(user, userFromDb);
    }

    @Test
    public void testFindUsernameByProjectId() {
        User userFromDb = userDao.findByProjectId(project.getId());
        assertEquals(user, userFromDb);
    }

    @Test
    public void testFindUsernameByTaskId() {
        User userFromDb = userDao.findByTaskId(task.getId());
        assertEquals(user, userFromDb);
    }
}