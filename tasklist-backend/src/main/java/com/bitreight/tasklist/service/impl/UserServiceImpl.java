package com.bitreight.tasklist.service.impl;

import com.bitreight.tasklist.dao.UserDao;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedUserException;
import com.bitreight.tasklist.dto.UserDto;
import com.bitreight.tasklist.entity.User;
import com.bitreight.tasklist.service.UserService;
import com.bitreight.tasklist.service.converter.UserDtoConverter;
import com.bitreight.tasklist.service.exception.ServiceUserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserDtoConverter userConverter;

    @Override
    public void register(UserDto userDto) throws ServiceUserAlreadyExistsException {
        if(userDto != null) {
            User user = userConverter.convertDto(userDto);
            user.setId(0);
            try {
                userDao.save(user);

            } catch (DaoSaveDuplicatedUserException e) {
                throw new ServiceUserAlreadyExistsException("Can't create user.", e);
            }
        }
    }

    @Override
    public UserDto checkCredentials(UserDto userDto) {
        UserDto actualUser = null;

        if(userDto != null) {
            User user = userDao.findByUsernameAndPassword(userDto.getUsername(), userDto.getPassword());

            if(user != null) {
                actualUser = userConverter.convertEntity(user);
            }
        }

        return actualUser;
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

            if(user != null) {
                userDto = userConverter.convertEntity(user);
            }
        }

        return userDto;
    }
}