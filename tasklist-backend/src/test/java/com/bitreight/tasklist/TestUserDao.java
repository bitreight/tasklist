package com.bitreight.tasklist;

import com.bitreight.tasklist.config.JpaConfiguration;
import com.bitreight.tasklist.dao.UserDao;
import com.bitreight.tasklist.entity.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JpaConfiguration.class, DaoContextConfiguration.class },
        loader = AnnotationConfigContextLoader.class)
public class TestUserDao {

    @Autowired
    private UserDao userDao;

    @Test
    @Transactional
    public void testCreateUser() {
        User user = new User();
        user.setUsername("alex");
        user.setPassword("pass");
        userDao.save(user);

        User userFromDb = userDao.findById(user.getId());

        assertTrue(user.equals(userFromDb));
    }
}
