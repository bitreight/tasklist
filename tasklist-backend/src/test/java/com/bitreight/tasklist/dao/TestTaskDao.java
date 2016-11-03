package com.bitreight.tasklist.dao;

import com.bitreight.tasklist.config.DaoContextConfiguration;
import com.bitreight.tasklist.config.JpaConfiguration;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.Task;
import com.bitreight.tasklist.entity.TaskPriority;
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

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JpaConfiguration.class, DaoContextConfiguration.class },
        loader = AnnotationConfigContextLoader.class)
@Transactional
public class TestTaskDao {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private TaskDao taskDao;

    private User user;
    private Project project;
    private List<Task> tasks;

    @Before
    public void setUp() {
        user = new User();
        user.setUsername("username");
        user.setPassword("pass");
        userDao.save(user);

        project = new Project();
        project.setTitle("project");
        project.setUser(user);
        projectDao.save(project);

        tasks = new ArrayList<>();

        Task task1 = new Task();
        task1.setTitle("task_one");
        task1.setDescription("description");
        task1.setDeadline(new Date(Calendar.getInstance().getTime().getTime()));
        task1.setPriority(TaskPriority.LOW);
        task1.setCompleted(false);
        task1.setProject(project);
        tasks.add(task1);

        Task task2 = new Task();
        task2.setTitle("task_two");
        task2.setDescription("description");
        task2.setDeadline(new Date(Calendar.getInstance().getTime().getTime()));
        task2.setPriority(TaskPriority.HIGH);
        task2.setCompleted(true);
        task2.setProject(project);
        tasks.add(task2);

        tasks.forEach(task -> taskDao.save(task));
    }

    @After
    public void tearDown() {
        tasks.forEach(task -> taskDao.deleteById(task.getId()));
        projectDao.deleteById(project.getId());
        userDao.deleteById(user.getId());
    }

    @Test
    public void testFindTaskById() {
        Task taskFromDb = taskDao.findById(tasks.get(0).getId());
        assertEquals(tasks.get(0), taskFromDb);
    }

    @Test
    public void testFindTaskByProject() {
        List<Task> tasksFromDb = taskDao.findByProject(project);
        assertEquals(tasks, tasksFromDb);
    }

    @Test
    public void testUpdateTask() {
        for(Task task : tasks) {
            task.setTitle("test" + tasks.indexOf(task));
            task.setDescription("test_description");
            task.setDeadline(new Date(Calendar.getInstance().getTime().getTime()));
            task.setPriority(TaskPriority.NORMAL);
            task.setCompleted(!task.isCompleted());
            taskDao.update(task);
        }

        List<Task> tasksFromDb = taskDao.findByProject(project);

        assertEquals(tasks, tasksFromDb);
    }

    @Test
    public void testSetTaskIsCompleted() {
        Task task = tasks.get(0);

        taskDao.setIsCompleted(task.getId(), true);

        Task taskFromDb = taskDao.findById(task.getId());

        assertTrue(taskFromDb.isCompleted());
    }
}