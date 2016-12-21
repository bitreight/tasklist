package com.bitreight.tasklist.service.impl;

import com.bitreight.tasklist.dao.UserDao;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedUserException;
import com.bitreight.tasklist.dto.ChangeUserPasswordDto;
import com.bitreight.tasklist.dto.UserDto;
import com.bitreight.tasklist.dto.UserProfileDto;
import com.bitreight.tasklist.entity.User;
import com.bitreight.tasklist.service.ImageService;
import com.bitreight.tasklist.service.UserService;
import com.bitreight.tasklist.service.converter.UserDtoConverter;
import com.bitreight.tasklist.service.converter.UserProfileDtoConverter;
import com.bitreight.tasklist.service.exception.ServiceInvalidUserPasswordException;
import com.bitreight.tasklist.service.exception.ServiceProfileImageUploadException;
import com.bitreight.tasklist.service.exception.ServiceUserAlreadyExistsException;
import com.bitreight.tasklist.service.exception.ServiceUserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service("userService")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserDtoConverter userConverter;

    @Autowired
    private UserProfileDtoConverter userProfileDtoConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImageService imageService;

    @Override
    public void register(UserDto userDto) throws ServiceUserAlreadyExistsException {
        if(userDto == null) {
            throw new IllegalArgumentException("userProfileDto cannot be null.");
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

    @Override
    public void update(UserProfileDto userProfileDto) throws ServiceUserNotFoundException {
        if(userProfileDto == null) {
            throw new IllegalArgumentException("userProfileDto cannot be null.");
        }

        User userFromDb = userDao.findById(userProfileDto.getId());
        if(userFromDb == null) {
            throw new ServiceUserNotFoundException("User not found.");
        }

        userFromDb.setName(userProfileDto.getName());
        userFromDb.setSurname(userProfileDto.getSurname());
    }

    @Override
    public void changePassword(ChangeUserPasswordDto passwordDto)
            throws ServiceUserNotFoundException, ServiceInvalidUserPasswordException {

        if(passwordDto == null) {
            throw new IllegalArgumentException("passwordDto cannot be null.");
        }

        User userFromDb = userDao.findById(passwordDto.getId());
        if(userFromDb == null) {
            throw new ServiceUserNotFoundException("User not found.");
        }

        String enteredPassword = passwordDto.getOldPassword();
        String currentPassword = userFromDb.getPassword();
        if(!passwordEncoder.matches(enteredPassword, currentPassword)) {
            throw new ServiceInvalidUserPasswordException("Invalid password.");
        }

        String newEncodedPassword = passwordEncoder.encode(passwordDto.getNewPassword());
        userFromDb.setPassword(newEncodedPassword);
    }

    @Override
    public String setProfileImage(byte[] profileImage, int userId)
            throws ServiceProfileImageUploadException, ServiceUserNotFoundException {

        if(profileImage == null) {
            throw new IllegalArgumentException("profileImage cannot be null.");
        }

        User userFromDb = userDao.findById(userId);
        if(userFromDb == null) {
            throw new ServiceUserNotFoundException("User not found.");
        }

        String profileImagePath = null;
        try {
            profileImagePath = imageService.uploadImage(profileImage);

        } catch (IOException e) {
            throw new ServiceProfileImageUploadException("Error happened while uploading image.", e);
        }

        userFromDb.setProfileImagePath(profileImagePath);
        return profileImagePath;
    }

    @Override
    public UserProfileDto getById(int userId) throws ServiceUserNotFoundException {
        User userFromDb = userDao.findById(userId);
        if(userFromDb == null) {
            throw new ServiceUserNotFoundException("User not found.");
        }
        return userProfileDtoConverter.convertEntity(userFromDb);
    }
}