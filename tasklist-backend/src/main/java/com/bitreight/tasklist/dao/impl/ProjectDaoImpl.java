package com.bitreight.tasklist.dao.impl;

import com.bitreight.tasklist.dao.ProjectDao;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedProjectException;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.User;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("projectDao")
@Transactional(rollbackFor = Exception.class)
public class ProjectDaoImpl implements ProjectDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Project project) throws DaoSaveDuplicatedProjectException {
        try {
            entityManager.persist(project);

        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new DaoSaveDuplicatedProjectException(
                                "Saving project with title \"" + project.getTitle() + "\"." +
                                " Already exists.", e);
            }
        }
    }

    @Override
    public void update(Project project) throws DaoSaveDuplicatedProjectException {
        Project projectFromDb = findById(project.getId());
        try {
            if (projectFromDb != null) {
                projectFromDb.setTitle(project.getTitle());
                projectFromDb.setDescription(project.getDescription());
                entityManager.flush();
            }

        } catch(PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new DaoSaveDuplicatedProjectException(
                                "Updating project with new title \"" + project.getTitle() + "\"." +
                                " Already exists.", e);
            }
        }
    }

    @Override
    public void deleteById(int projectId) {
        Project projectFromDb = findById(projectId);
        if(projectFromDb != null) {
            entityManager.remove(projectFromDb);
        }
    }

    @Override
    public Project findById(int projectId) {
        return entityManager.find(Project.class, projectId);
    }

    @Override
    public List<Project> findByUser(User user) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Project> criteria = builder.createQuery(Project.class);
        Root<Project> root = criteria.from(Project.class);
        criteria.where(builder.equal(root.get("user"), user));

        return entityManager.createQuery(criteria).getResultList();
    }
}