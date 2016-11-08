package com.bitreight.tasklist.dao.impl;

import com.bitreight.tasklist.dao.TaskDao;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.Task;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("taskDao")
public class TaskDaoImpl implements TaskDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Task task) {
        entityManager.persist(task);
    }

    @Override
    public void update(Task task) {
        Task taskFromDb = findById(task.getId());
        if(taskFromDb != null) {
            taskFromDb.setTitle(task.getTitle());
            taskFromDb.setDescription(task.getDescription());
            taskFromDb.setDeadline(task.getDeadline());
            taskFromDb.setPriority(task.getPriority());
        }
    }

    @Override
    public void deleteById(int taskId) {
        entityManager.remove(findById(taskId));
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
        findById(taskId).setCompleted(isCompleted);
    }
}
