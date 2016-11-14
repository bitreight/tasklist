package com.bitreight.tasklist.service.impl;

import com.bitreight.tasklist.dao.UserDao;
import com.bitreight.tasklist.dto.UserDto;
import com.bitreight.tasklist.entity.User;
import com.bitreight.tasklist.service.UserService;
import com.bitreight.tasklist.service.converter.UserDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserDtoConverter userConverter;

    @Override
    public void register(UserDto userDto) {
        if(userDto != null) {
            User user = userConverter.convertDto(userDto);
            user.setId(0);
            userDao.save(user);
        }
    }

    @Override
    public UserDto checkCredentials(UserDto userDto) {
        if(userDto != null) {
            User user = userDao.findByUsernameAndPassword(userDto.getUsername(), userDto.getPassword());
            return userConverter.convertEntity(user);
        }
        return null;
    }

    @Override
    public void update(UserDto userDto) {
        if(userDto != null) {
            User user = userConverter.convertDto(userDto);
            userDao.update(user);
        }
    }

    @Override
    public UserDto getInfoById(int userId) {
        UserDto userDto = null;

        if(userId > 0) {
            User user = userDao.findById(userId);
            userDto = userConverter.convertEntity(user);
        }

        return userDto;
    }
}