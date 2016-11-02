package com.bitreight.tasklist.dao;

import com.bitreight.tasklist.entity.Project;
import com.bitreight.tasklist.entity.User;

import java.util.List;

public interface ProjectDao {
    void save(Project project);

    void update(Project project);

    void deleteById(int projectId);

    Project findById(int projectId);

    List<Project> findByUser(User user);
}
