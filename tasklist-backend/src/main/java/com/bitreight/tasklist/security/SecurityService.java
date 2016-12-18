package com.bitreight.tasklist.security;

import com.bitreight.tasklist.dao.UserDao;
import com.bitreight.tasklist.service.exception.ServiceProjectNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceTaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("securityService")
@Transactional(readOnly = true)
public class SecurityService {

    @Autowired
    private UserDao userDao;

    public boolean isProjectOwner(int projectId) throws ServiceProjectNotFoundException {
        com.bitreight.tasklist.entity.User projectOwner = userDao.findByProjectId(projectId);

        return projectOwner == null || projectOwner.getUsername().equals(getLoggedInUsername());

    }

    public boolean isTaskOwner(int taskId) throws ServiceTaskNotFoundException {
        com.bitreight.tasklist.entity.User taskOwner = userDao.findByTaskId(taskId);

        return taskOwner == null || taskOwner.getUsername().equals(getLoggedInUsername());

    }

    private String getLoggedInUsername() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }
}
