package com.bitreight.tasklist.dao.impl;

import com.bitreight.tasklist.dao.UserDao;
import com.bitreight.tasklist.entity.User;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public void update(User user) {
        User userFromDb = findById(user.getId());
        if(userFromDb != null) {
            userFromDb.setName(user.getName());
            userFromDb.setSurname(user.getSurname());
            userFromDb.setPassword(user.getPassword());
        }
    }

    @Override
    public void deleteById(int userId) {
        User userFromDb = findById(userId);
        if(userFromDb != null) {
            entityManager.remove(userFromDb);
        }
    }

    @Override
    public User findById(int userId) {
        return entityManager.find(User.class, userId);
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        List<User> users = (List<User>) entityManager.createQuery("Select u from User u where u.username LIKE :username " +
                                                "and u.password LIKE :password")
                                    .setParameter("username", username)
                                    .setParameter("password", password)
                                    .getResultList();
        return users.isEmpty() ? null : users.get(0);
    }
}
