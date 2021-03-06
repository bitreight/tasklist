package com.bitreight.tasklist.dao;

import com.bitreight.tasklist.config.BackendConfiguration;
import com.bitreight.tasklist.config.DaoContextConfiguration;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedProjectException;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedTaskException;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedUserException;
import com.bitreight.tasklist.dao.exception.DaoUpdateNonActualVersionOfTaskException;
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

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.bitreight.tasklist.entity.SortKey.DEADLINE;
import static com.bitreight.tasklist.entity.SortKey.PRIORITY;
import static com.bitreight.tasklist.entity.SortKey.TITLE;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.next;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BackendConfiguration.class, DaoContextConfiguration.class },
        loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("dev")
@Transactional
public class TestTaskDao {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private TaskFindDao taskFindDao;

    private User user;
    private Project project;
    private List<Task> tasks;

    @Before
    public void setUp() throws DaoSaveDuplicatedTaskException, DaoSaveDuplicatedUserException,
            DaoSaveDuplicatedProjectException {
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
        task1.setDeadline(LocalDate.now());    //today
        task1.setPriority(TaskPriority.HIGH);
        task1.setCompleted(false);
        task1.setProject(project);
        tasks.add(task1);

        Task task2 = new Task();
        task2.setTitle("task_two");
        task2.setDescription("description");
        task2.setDeadline(LocalDate.now().with(next(SUNDAY)));  //nearest sunday
        task2.setPriority(TaskPriority.LOW);
        task2.setCompleted(true);
        task2.setProject(project);
        tasks.add(task2);

        for(Task task : tasks) {
            taskDao.save(task);
        }
    }

    @After
    public void tearDown() {
        tasks.forEach(task -> taskDao.delete(task));
        projectDao.delete(project);
        userDao.delete(user);
    }

    @Test(expected = DaoSaveDuplicatedTaskException.class)
    public void testSaveTask_withDuplicatedName() throws DaoSaveDuplicatedTaskException {
        Task duplicatedTask = new Task();
        duplicatedTask.setTitle(tasks.get(0).getTitle());
        duplicatedTask.setPriority(tasks.get(0).getPriority());
        duplicatedTask.setProject(project);
        taskDao.save(duplicatedTask);
    }

    @Test
    public void testFindTaskById() {
        Task taskFromDb = taskDao.findById(tasks.get(0).getId());
        assertEquals(tasks.get(0), taskFromDb);
    }

    @Test
    public void testFindTaskById_invalidId() {
        Task taskFromDb = taskDao.findById(-1);
        assertNull(taskFromDb);
    }

    @Test
    @Ignore
    public void testUpdateTask() throws DaoUpdateNonActualVersionOfTaskException,
            DaoSaveDuplicatedTaskException {
        for(Task task : tasks) {
            task.setTitle("test" + tasks.indexOf(task));
            task.setDescription("test_description");
            task.setDeadline(LocalDate.now());
            task.setPriority(TaskPriority.NORMAL);
            task.setCompleted(!task.isCompleted());
            taskDao.update(task);
        }

        List<Task> tasksFromDb = taskFindDao
                .findByProjectAndPeriod(project, null, null,
                        Collections.singletonList(TITLE.toString().toLowerCase()));

        assertEquals(tasks, tasksFromDb);
    }

    @Test(expected = DaoSaveDuplicatedTaskException.class)
    public void testUpdateTask_withDuplicatedTitle() throws DaoUpdateNonActualVersionOfTaskException,
            DaoSaveDuplicatedTaskException {
        Task duplicatedTask = tasks.get(1);
        duplicatedTask.setTitle(tasks.get(0).getTitle());
        taskDao.update(duplicatedTask);
    }

    @Test(expected = DaoUpdateNonActualVersionOfTaskException.class)
    public void testUpdateTask_withNonActualVersion() throws DaoUpdateNonActualVersionOfTaskException,
            DaoSaveDuplicatedTaskException {
        //get task from db
        Task taskFromDb = taskDao.findById(tasks.get(0).getId());

        //create its copy with current version
        Task taskOldVersion = new Task();
        taskOldVersion.setId(taskFromDb.getId());
        taskOldVersion.setTitle(taskFromDb.getTitle());
        taskOldVersion.setDescription(taskFromDb.getDescription());
        taskOldVersion.setDeadline(taskFromDb.getDeadline());
        taskOldVersion.setPriority(taskFromDb.getPriority());
        taskOldVersion.setCompleted(taskFromDb.isCompleted());
        taskOldVersion.setProject(taskFromDb.getProject());

        //update task from db (version is changed)
        taskFromDb.setDescription("new_description");
        taskDao.update(taskFromDb);

        taskDao.update(taskOldVersion);
    }

    ///////////////////////////////////////////////////////////////////////////////////
    @Test
    public void testFindTaskByUserAndPeriod_todayAndSortByPriority() {
        LocalDate today = LocalDate.now();
        List<Task> todayTasksFromDb = taskFindDao
                .findByUserAndPeriod(user, today, today,
                        Collections.singletonList(PRIORITY.toString().toLowerCase()));

        assertEquals(todayTasksFromDb.size(), 1);
        assertEquals(todayTasksFromDb.get(0), tasks.get(0));
    }

    @Test
    public void testFindTaskByUserAndPeriod_fromTodayToSundayAndSortByDeadline() {
        LocalDate today = LocalDate.now();
        LocalDate sunday = LocalDate.now().with(next(SUNDAY));
        List<Task> sundayTasksFromDb = taskFindDao
                .findByUserAndPeriod(user, today, sunday,
                        Collections.singletonList(DEADLINE.toString().toLowerCase()));
        assertEquals(sundayTasksFromDb, tasks);
    }

    @Test
    @Ignore
    public void testFindTaskByUserAndPeriod_allTasksAndSortByTitle() {
        List<Task> tasksFromDb = taskFindDao
                .findByUserAndPeriod(user, null, null, Collections.singletonList(TITLE.toString().toLowerCase()));
        assertEquals(tasks, tasksFromDb);
    }

    @Test
    public void testFindTaskByUserAndPeriod_yesterdayAndSortByTitle() {
        LocalDate yesterday = LocalDate.now().minus(Period.ofDays(1));
        List<Task> tasksFromDb = taskFindDao
                .findByUserAndPeriod(user, yesterday, yesterday,
                        Collections.singletonList(TITLE.toString().toLowerCase()));
        assertTrue(tasksFromDb.isEmpty());
    }

    @Test
    public void testFindTaskByProjectAndPeriod_todayAndSortByPriority() {
        LocalDate today = LocalDate.now();
        List<Task> todayTasksFromDb = taskFindDao
                .findByProjectAndPeriod(project, today, today,
                        Collections.singletonList(PRIORITY.toString().toLowerCase()));

        assertEquals(todayTasksFromDb.size(), 1);
        assertEquals(todayTasksFromDb.get(0), tasks.get(0));
    }

    @Test
    public void testFindTaskByProjectAndPeriod_fromTodayToSundayAndSortByDeadline() {
        LocalDate today = LocalDate.now();
        LocalDate sunday = LocalDate.now().with(next(SUNDAY));
        List<Task> sundayTasksFromDb = taskFindDao
                .findByProjectAndPeriod(project, today, sunday,
                        Collections.singletonList(DEADLINE.toString().toLowerCase()));
        assertEquals(sundayTasksFromDb, tasks);
    }

    @Test
    @Ignore
    public void testFindTaskByProjectAndPeriod_allTasksAndSortByTitle() {
        List<Task> tasksFromDb = taskFindDao
                .findByProjectAndPeriod(project, null, null, Collections.singletonList(TITLE.toString().toLowerCase()));
        assertEquals(tasks, tasksFromDb);
    }

    @Test
    public void testFindTaskByProjectAndPeriod_yesterdayAndSortByTitle() {
        LocalDate yesterday = LocalDate.now().minus(Period.ofDays(1));
        List<Task> tasksFromDb = taskFindDao
                .findByProjectAndPeriod(project, yesterday, yesterday,
                        Collections.singletonList(TITLE.toString().toLowerCase()));
        assertTrue(tasksFromDb.isEmpty());
    }
}