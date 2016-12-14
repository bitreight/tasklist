package com.bitreight.tasklist.dao.impl;

import com.bitreight.tasklist.dao.TaskFindDao;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.Task;
import com.bitreight.tasklist.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

@Repository("taskSearchDao")
@Transactional(rollbackFor = Exception.class)
public class TaskFindDaoImpl implements TaskFindDao {

    @PersistenceContext
    private EntityManager entityManager;

    //TODO: refactor to find tasks of the period (min date, max date)
    @Override
    public List<Task> findByUserAndMaxDeadline(User user, Date deadline, List<String> orderFields) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Task> query = builder.createQuery(Task.class);
        Root<Task> task = query.from(Task.class);

        Join<Task, Project> taskProject = task.join("project");
        Join<Project, User> taskProjectUser = taskProject.join("user");

        List<Predicate> predicates = new ArrayList<>();
        addEqualPredicate(predicates, builder, taskProjectUser, "username", user.getUsername());
        addLessThanOrEqualToPredicate(predicates, builder, task, "deadline", deadline);

        List<Order> orders = new ArrayList<>();
        orderFields.forEach(field -> addAscOrder(orders, builder, task, field));

        query.where(predicates.toArray(new Predicate[0])).orderBy(orders);

        return entityManager.createQuery(query).getResultList();
    }

    //TODO: refactor to find tasks of the period (min date, max date)
    @Override
    public List<Task> findByProjectAndMaxDeadline(Project project, Date deadline, List<String> orderFields) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Task> query = builder.createQuery(Task.class);
        Root<Task> task = query.from(Task.class);

        List<Predicate> predicates = new ArrayList<>();
        addEqualPredicate(predicates, builder, task, "project", project);
        addLessThanOrEqualToPredicate(predicates, builder, task, "deadline", deadline);

        List<Order> orders = new ArrayList<>();
        orderFields.forEach(field -> addAscOrder(orders, builder, task, field));

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
