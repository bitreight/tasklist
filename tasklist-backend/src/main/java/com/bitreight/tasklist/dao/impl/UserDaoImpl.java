package com.bitreight.tasklist.dao.impl;

import com.bitreight.tasklist.dao.UserDao;
import com.bitreight.tasklist.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public void delete(int userId) {

    }

    @Override
    public User findById(int userId) {
        return entityManager.find(User.class, userId);
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        return null;
    }
}
