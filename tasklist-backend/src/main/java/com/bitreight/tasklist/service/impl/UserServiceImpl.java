package com.bitreight.tasklist.service.impl;

import com.bitreight.tasklist.dao.UserDao;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedUserException;
import com.bitreight.tasklist.dto.ChangePasswordDto;
import com.bitreight.tasklist.dto.UserDto;
import com.bitreight.tasklist.entity.User;
import com.bitreight.tasklist.service.UserService;
import com.bitreight.tasklist.service.converter.UserDtoConverter;
import com.bitreight.tasklist.service.exception.ServiceInvalidUserPasswordException;
import com.bitreight.tasklist.service.exception.ServiceUserAlreadyExistsException;
import com.bitreight.tasklist.service.exception.ServiceUserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserDtoConverter userConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void register(UserDto userDto) throws ServiceUserAlreadyExistsException {
        if(userDto == null) {
            throw new IllegalArgumentException("userDto cannot be null.");
        }

        User userToRegister = userConverter.convertDto(userDto);

        userToRegister.setId(0);
        userToRegister.setPassword(passwordEncoder.encode(userDto.getPassword()));

        try {
            userDao.save(userToRegister);

        } catch (DaoSaveDuplicatedUserException e) {
            throw new ServiceUserAlreadyExistsException("Can't create user.", e);
        }
    }

//    @Override
//    public UserDto getByUsername(String username) throws ServiceUserNotFoundException {
//
//        if(username == null) {
//            throw new IllegalArgumentException("Username cannot be null.");
//        }
//
//        User userFromDb = userDao.findByUsername(username);
//        if(userFromDb == null) {
//            throw new ServiceUserNotFoundException("User not found.");
//        }
//
//        return userConverter.convertEntity(userFromDb);
//    }

    @Override
    public void update(UserDto userDto) throws ServiceUserNotFoundException {
        if(userDto == null) {
            throw new IllegalArgumentException("userDto cannot be null.");
        }

        User userFromDb = userDao.findById(userDto.getId());
        if(userFromDb == null) {
            throw new ServiceUserNotFoundException("User not found.");
        }

//        User userToUpdate = userConverter.convertDto(userDto);
//        userDao.update(userToUpdate);

        userFromDb.setName(userDto.getName());
        userFromDb.setSurname(userDto.getSurname());
    }

    @Override
    public void changePassword(ChangePasswordDto passwordDto)
            throws ServiceUserNotFoundException, ServiceInvalidUserPasswordException {

        if(passwordDto == null) {
            throw new IllegalArgumentException("passwordDto cannot be null.");
        }

        User userFromDb = userDao.findById(passwordDto.getUserId());
        if(userFromDb == null) {
            throw new ServiceUserNotFoundException("User not found.");
        }

        if(!userFromDb.getPassword().equals(passwordDto.getOldPassword())) {
            throw new ServiceInvalidUserPasswordException("Invalid password.");
        }

        userFromDb.setPassword(passwordDto.getNewPassword());
    }

    @Override
    public UserDto getById(int userId) throws ServiceUserNotFoundException {
        User userFromDb = userDao.findById(userId);
        if(userFromDb == null) {
            throw new ServiceUserNotFoundException("User not found.");
        }
        return userConverter.convertEntity(userFromDb);
    }
}