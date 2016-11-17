package com.bitreight.tasklist.dao;

import com.bitreight.tasklist.config.BackendConfiguration;
import com.bitreight.tasklist.config.DaoContextConfiguration;
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

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setUsername("username");
        user.setPassword("pass");
        user.setName("name");
        user.setSurname("surname");
        userDao.save(user);
    }

    @After
    public void tearDown() {
        userDao.deleteById(user.getId());
    }

    @Test
    public void testFindById() {
        User userFromDb = userDao.findById(user.getId());
        assertEquals(user, userFromDb);
    }

    @Test
    public void testFindById_nonExistentId() {
        User userFromDb = userDao.findById(-1);
        assertNull(userFromDb);
    }

    @Test
    public void testFindByUsernameAndPassword() {
        User userFromDb = userDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        assertEquals(user, userFromDb);
    }

    @Test
    public void testFindByUsernameAndPassword_nonExistentUsernameAndPassword() {
        User userFromDb = userDao.findByUsernameAndPassword("", "");
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
    public void testUpdateUser_nonexistentUser() {
        User invalidUser = new User();

        userDao.update(invalidUser);
        User userFromDb = userDao.findById(user.getId());

        assertEquals(user, userFromDb);
    }

    @Test
    public void testDeleteById_nonexistentId() {
        userDao.deleteById(-1);
        User userFromDb = userDao.findById(user.getId());
        assertEquals(user, userFromDb);
    }
}