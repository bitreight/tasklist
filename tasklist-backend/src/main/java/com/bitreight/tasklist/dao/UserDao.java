package com.bitreight.tasklist.dao;

import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedUserException;
import com.bitreight.tasklist.entity.User;

public interface UserDao {
    void save(User user) throws DaoSaveDuplicatedUserException;

    void update(User user);

    void delete(User user);

    User findById(int userId);

    User findByUsername(String username);
}
