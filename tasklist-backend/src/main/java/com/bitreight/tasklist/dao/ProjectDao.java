package com.bitreight.tasklist.dao;

import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedProjectException;
import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.User;

import java.util.List;

public interface ProjectDao {
    void save(Project project) throws DaoSaveDuplicatedProjectException;

    void update(Project project) throws DaoSaveDuplicatedProjectException;

    void delete(Project project);

    Project findById(int projectId);

    List<Project> findByUser(User user);
}
