package com.bitreight.tasklist;

import com.bitreight.tasklist.config.JpaConfiguration;
import com.bitreight.tasklist.dao.UserDao;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JpaConfiguration.class, DaoContextConfiguration.class },
        loader = AnnotationConfigContextLoader.class)
@Transactional
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

    public void testFindByUsernameAndPassword() {
        User userFromDb = userDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        assertEquals(user, userFromDb);
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
}