package com.bitreight.tasklist.dao.impl;

import com.bitreight.tasklist.dao.TaskDao;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedTaskException;
import com.bitreight.tasklist.dao.exception.DaoUpdateNonActualVersionOfTaskException;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.Task;
import com.bitreight.tasklist.entity.User;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.sql.Date;
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

    @Override
    public List<Task> findByUserAndMaxDate(User user, Date date, String sortField) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Task> query = builder.createQuery(Task.class);
        Root<Task> task = query.from(Task.class);

        Join<Task, Project> taskProject = task.join("project");
        Join<Project, User> taskProjectUser = taskProject.join("user");

        Order order = null;
        if(sortField == null) {
            order = builder.asc(task.get("title"));
        } else {
            order = builder.asc(task.get(sortField));
        }

        query.where(builder.equal(taskProjectUser.get("username"), user.getUsername()),
                    builder.lessThanOrEqualTo(task.get("deadline"), date))
             .orderBy(order);

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Task> findByProjectAndMaxDate(Project project, Date date, String sortField) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Task> query = builder.createQuery(Task.class);
        Root<Task> task = query.from(Task.class);

        query.where(builder.equal(task.get("project"), project),
                    builder.lessThanOrEqualTo(task.get("deadline"), date))
             .orderBy(builder.asc(task.get(sortField)));

        return entityManager.createQuery(query).getResultList();
    }

    private CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }
}
