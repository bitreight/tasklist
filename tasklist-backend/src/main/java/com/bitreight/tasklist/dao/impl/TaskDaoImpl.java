package com.bitreight.tasklist.dao.impl;

import com.bitreight.tasklist.dao.TaskDao;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedTaskException;
import com.bitreight.tasklist.dao.exception.DaoUpdateNonActualVersionOfTaskException;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.Task;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

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
                        "Task with title \"" + task.getTitle() +
                                "\" already exists in the database.", e);
            }
        }
    }

    @Override
    public void update(Task task) throws DaoUpdateNonActualVersionOfTaskException {
        Task taskFromDb = findById(task.getId());
        try {
            if (taskFromDb != null) {
                entityManager.merge(task);
            }
        } catch (OptimisticLockException e) {
            throw new DaoUpdateNonActualVersionOfTaskException(
                            "Version of the task id=" + task.getId() + " has been changed. " +
                            "Old version is " + task.getVersion() +
                            ", new version is " + taskFromDb.getVersion() + ".", e);
        }
    }

    @Override
    public void deleteById(int taskId) {
        Task taskFromDb = findById(taskId);
        if(taskFromDb != null) {
            entityManager.remove(taskFromDb);
        }
    }

    @Override
    public Task findById(int taskId) {
        return entityManager.find(Task.class, taskId);
    }

    @Override
    public List<Task> findByProject(Project project) {
        return (List<Task>) entityManager.createQuery("Select t from Task t where t.project LIKE :project")
                                            .setParameter("project", project)
                                            .getResultList();
    }

    @Override
    public void setIsCompleted(int taskId, boolean isCompleted) {
        Task taskFromDb = findById(taskId);
        if(taskFromDb != null) {
            taskFromDb.setCompleted(isCompleted);
        }
    }
}
