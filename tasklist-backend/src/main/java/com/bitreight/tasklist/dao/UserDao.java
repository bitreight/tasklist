package com.bitreight.tasklist.dao;

import com.bitreight.tasklist.entity.User;

public interface UserDao {
    void save(User user);
    void delete(int userId);
    User findById(int userId);
    User findByUsernameAndPassword(String username, String password);
}
