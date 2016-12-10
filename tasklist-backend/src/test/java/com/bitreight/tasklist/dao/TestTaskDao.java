package com.bitreight.tasklist.dao;

import com.bitreight.tasklist.config.BackendConfiguration;
import com.bitreight.tasklist.config.DaoContextConfiguration;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedProjectException;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedTaskException;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedUserException;
import com.bitreight.tasklist.dao.exception.DaoUpdateNonActualVersionOfTaskException;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.SortKey;
import com.bitreight.tasklist.entity.Task;
import com.bitreight.tasklist.entity.TaskPriority;
import com.bitreight.tasklist.entity.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
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
        task1.setDeadline(Date.valueOf(LocalDate.now()));    //today
        task1.setPriority(TaskPriority.HIGH);
        task1.setCompleted(false);
        task1.setProject(project);
        tasks.add(task1);

        Task task2 = new Task();
        task2.setTitle("task_two");
        task2.setDescription("description");
        task2.setDeadline(Date.valueOf(LocalDate.now().with(next(SUNDAY))));  //nearest sunday
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

    @Test
    public void testFindTaskByUserAndMaxDate_todayAndSortByPriority() {
        Date today = Date.valueOf(LocalDate.now());
        List<Task> todayTasksFromDb = taskDao
                .findByUserAndMaxDeadline(user, today, PRIORITY.toString().toLowerCase());

        assertEquals(todayTasksFromDb.size(), 1);
        assertEquals(todayTasksFromDb.get(0), tasks.get(0));
    }

    @Test
    public void testFindTaskByUserAndMaxDate_sundayAndSortByDeadline() {
        Date sunday = Date.valueOf(LocalDate.now().with(next(SUNDAY)));
        List<Task> sundayTasksFromDb = taskDao
                .findByUserAndMaxDeadline(user, sunday, DEADLINE.toString().toLowerCase());
        assertEquals(sundayTasksFromDb, tasks);
    }

    @Test
    public void testFindTaskByUserAndMaxDate_allTasksAndSortByTitle() {
        Date farFuture = new Date(Long.MAX_VALUE);
        List<Task> tasksFromDb = taskDao
                .findByUserAndMaxDeadline(user, farFuture, TITLE.toString().toLowerCase());
        assertEquals(tasks, tasksFromDb);
    }

    @Test
    public void testFindTaskByUserAndMaxDate_yesterdayAndSortByTitle() {
        Date yesterday = Date.valueOf(LocalDate.now().minus(Period.ofDays(1)));
        List<Task> tasksFromDb = taskDao
                .findByUserAndMaxDeadline(user, yesterday, TITLE.toString().toLowerCase());
        assertTrue(tasksFromDb.isEmpty());
    }

    @Test
    public void testFindTaskByProjectAndMaxDate_todayAndSortByPriority() {
        Date today = Date.valueOf(LocalDate.now());
        List<Task> todayTasksFromDb = taskDao
                .findByProjectAndMaxDeadline(project, today, PRIORITY.toString().toLowerCase());

        assertEquals(todayTasksFromDb.size(), 1);
        assertEquals(todayTasksFromDb.get(0), tasks.get(0));
    }

    @Test
    public void testFindTaskByProjectAndMaxDate_sundayAndSortByDeadline() {
        Date sunday = Date.valueOf(LocalDate.now().with(next(SUNDAY)));
        List<Task> sundayTasksFromDb = taskDao
                .findByProjectAndMaxDeadline(project, sunday, DEADLINE.toString().toLowerCase());
        assertEquals(sundayTasksFromDb, tasks);
    }

    @Test
    public void testFindTaskByProjectAndMaxDate_allTasksAndSortByTitle() {
        Date farFuture = new Date(Long.MAX_VALUE);
        List<Task> tasksFromDb = taskDao
                .findByProjectAndMaxDeadline(project, farFuture, TITLE.toString().toLowerCase());
        assertEquals(tasks, tasksFromDb);
    }

    @Test
    public void testFindTaskByProjectAndMaxDate_yesterdayAndSortByTitle() {
        Date yesterday = Date.valueOf(LocalDate.now().minus(Period.ofDays(1)));
        List<Task> tasksFromDb = taskDao
                .findByProjectAndMaxDeadline(project, yesterday, TITLE.toString().toLowerCase());
        assertTrue(tasksFromDb.isEmpty());
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
    public void testUpdateTask() throws DaoUpdateNonActualVersionOfTaskException,
            DaoSaveDuplicatedTaskException {
        for(Task task : tasks) {
            task.setTitle("test" + tasks.indexOf(task));
            task.setDescription("test_description");
            task.setDeadline(new Date(Calendar.getInstance().getTime().getTime()));
            task.setPriority(TaskPriority.NORMAL);
            task.setCompleted(!task.isCompleted());
            taskDao.update(task);
        }

        List<Task> tasksFromDb = taskDao
                .findByProjectAndMaxDeadline(project, new Date(Long.MAX_VALUE), TITLE.toString().toLowerCase());

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

    @Test
    public void testDate() {
        //System.out.println(LocalDate.now().with(next(SATURDAY)));
        SortKey.valueOf(null);
    }

//    @Test
//    public void testSetTaskIsCompleted() {
//        Task task = tasks.get(0);
//
//        taskDao.setIsCompleted(task.getId(), true);
//        Task taskFromDb = taskDao.findById(task.getId());
//
//        assertTrue(taskFromDb.isCompleted());
//    }
}