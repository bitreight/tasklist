package com.bitreight.tasklist.dao.impl;

import com.bitreight.tasklist.dao.UserDao;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedUserException;
import com.bitreight.tasklist.entity.User;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

@Repository("userDao")
@Transactional(rollbackFor = Exception.class)
@SuppressWarnings("unchecked")
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(User user) throws DaoSaveDuplicatedUserException {
        try {
            entityManager.persist(user);

        } catch (PersistenceException e) {
            if(e.getCause() instanceof ConstraintViolationException) {
                throw new DaoSaveDuplicatedUserException(
                                "User with username \"" + user.getUsername() +
                                "\" already exists in the database.", e);
            }
        }
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
    public void delete(User user) {
        entityManager.remove(user);
    }

    @Override
    public User findById(int userId) {
        return entityManager.find(User.class, userId);
    }

    @Override
    public User findByUsername(String username) {
        List<User> users = (List<User>) entityManager.createQuery("Select u from User u where u.username LIKE :username")
                                                                .setParameter("username", username)
                                                                .getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public User findByProjectId(int projectId) {
        List<User> users = (List<User>) entityManager.createQuery("Select up From Project p Join p.user up " +
                                                "Where p.id Like :projectId")
                                                .setParameter("projectId", projectId)
                                                .getResultList();
        return users.isEmpty() ? null : users.get(0);
    }


    @Override
    public User findByTaskId(int taskId) {
        List<User> users = (List<User>) entityManager.createQuery("Select utp From Task t Join t.project pt Join pt.user utp " +
                                                "Where t.id Like :taskId")
                                                .setParameter("taskId", taskId)
                                                .getResultList();
        return users.isEmpty() ? null : users.get(0);
    }
}
