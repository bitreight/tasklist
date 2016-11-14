package com.bitreight.tasklist.config;

import com.bitreight.tasklist.dao.ProjectDao;
import com.bitreight.tasklist.dao.TaskDao;
import com.bitreight.tasklist.dao.UserDao;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoMockConfiguration {

    @Bean
    public UserDao userDao() {
        return Mockito.mock(UserDao.class);
    }

    @Bean
    public ProjectDao projectDao() {
        return Mockito.mock(ProjectDao.class);
    }

    @Bean
    public TaskDao taskDao() {
        return Mockito.mock(TaskDao.class);
    }
}
