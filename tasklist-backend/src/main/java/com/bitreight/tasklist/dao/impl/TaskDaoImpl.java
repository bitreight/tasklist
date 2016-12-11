package com.bitreight.tasklist.dao.impl;

import com.bitreight.tasklist.dao.TaskDao;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedTaskException;
import com.bitreight.tasklist.dao.exception.DaoUpdateNonActualVersionOfTaskException;
import com.bitreight.tasklist.entity.Task;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

@Repository("taskDao")
@Transactional(rollbackFor = Exception.class)
public class TaskDaoImpl implements TaskDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Task task) throws DaoSaveDuplicatedTaskException {
        try {
            entityManager.persist(task);

        } catch (PersistenceException e) {
            if(e.getCause() instanceof ConstraintViolationException) {
                throw new DaoSaveDuplicatedTaskException(
                                "Saving task with title \"" + task.getTitle() + "\"." +
                                " Already exists.", e);
            }
        }
    }

    @Override
    public void update(Task task) throws DaoUpdateNonActualVersionOfTaskException,
            DaoSaveDuplicatedTaskException {
        try {
            entityManager.merge(task);
            entityManager.flush();

        } catch (OptimisticLockException e) {
            throw new DaoUpdateNonActualVersionOfTaskException(
                            "Version of the task id=" + task.getId() + " has been changed. " +
                            "(updated by another user.)");

        } catch (PersistenceException e) {
            if(e.getCause() instanceof ConstraintViolationException) {
                throw new DaoSaveDuplicatedTaskException(
                                "Updating task with new title \"" + task.getTitle() + "\"." +
                                " Already exists.", e);
            }
        }
    }

    @Override
    public void delete(Task task) {
        entityManager.remove(task);
    }

    @Override
    public Task findById(int taskId) {
        return entityManager.find(Task.class, taskId);
    }
}