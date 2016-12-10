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
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.util.ArrayList;
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
    public List<Task> findByUserAndMaxDeadline(User user, Date date, String orderField) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Task> query = builder.createQuery(Task.class);
        Root<Task> task = query.from(Task.class);

        Join<Task, Project> taskProject = task.join("project");
        Join<Project, User> taskProjectUser = taskProject.join("user");

        List<Predicate> predicates = new ArrayList<>();
        addEqualPredicate(predicates, builder, taskProjectUser, "username", user.getUsername());
        addLessThanOrEqualToPredicate(predicates, builder, task, "deadline", date);

        List<Order> orders = new ArrayList<>();
        addAscOrder(orders, builder, task, orderField);

        query.where(predicates.toArray(new Predicate[0])).orderBy(orders);

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Task> findByProjectAndMaxDeadline(Project project, Date date, String orderField) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Task> query = builder.createQuery(Task.class);
        Root<Task> task = query.from(Task.class);

        List<Predicate> predicates = new ArrayList<>();
        addEqualPredicate(predicates, builder, task, "project", project);
        addLessThanOrEqualToPredicate(predicates, builder, task, "deadline", date);

        List<Order> orders = new ArrayList<>();
        addAscOrder(orders, builder, task, orderField);

        query.where(predicates.toArray(new Predicate[0])).orderBy(orders);

        return entityManager.createQuery(query).getResultList();
    }

    private CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    private <Z, X, V> void addEqualPredicate(List<Predicate> predicates, CriteriaBuilder builder,
                                             From<Z, X> root, String field, V value) {
        Predicate equalPredicate = builder.equal(root.get(field), value);
        predicates.add(equalPredicate);
    }

    private <Z, X, V extends Comparable<V>> void addLessThanOrEqualToPredicate(List<Predicate> predicates, CriteriaBuilder builder,
                                                                               From<Z, X> root, String field, V value) {
        Predicate lessOrEqualPredicate = builder.lessThanOrEqualTo(root.get(field), value);
        predicates.add(lessOrEqualPredicate);
    }

    private <Z, X> void addAscOrder(List<Order> orders, CriteriaBuilder builder, From<Z, X> root, String field) {
        Order order = builder.asc(root.get(field));
        orders.add(order);
    }
}