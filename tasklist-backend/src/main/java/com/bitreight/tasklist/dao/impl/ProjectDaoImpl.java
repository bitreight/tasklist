package com.bitreight.tasklist.dao.impl;

import com.bitreight.tasklist.dao.ProjectDao;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("projectDao")
public class ProjectDaoImpl implements ProjectDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Project project) {
        entityManager.persist(project);
    }

    @Override
    public void update(Project project) {
        Project projectFromDb = findById(project.getId());
        projectFromDb.setTitle(project.getTitle());
        projectFromDb.setDescription(project.getDescription());
    }

    @Override
    public void deleteById(int projectId) {
        entityManager.remove(findById(projectId));
    }

    @Override
    public Project findById(int projectId) {
        return entityManager.find(Project.class, projectId);
    }

    @Override
    public List<Project> findByUser(User user) {
        return entityManager.createQuery("Select p from Project p where p.user LIKE :user")
                .setParameter("user", user)
                .getResultList();
    }
}
