package com.bitreight.tasklist.service;

import com.bitreight.tasklist.dto.ChangeUserPasswordDto;
import com.bitreight.tasklist.dto.UserDto;
import com.bitreight.tasklist.dto.UserProfileDto;
import com.bitreight.tasklist.service.exception.ServiceInvalidUserPasswordException;
import com.bitreight.tasklist.service.exception.ServiceProfileImageUploadException;
import com.bitreight.tasklist.service.exception.ServiceUserAlreadyExistsException;
import com.bitreight.tasklist.service.exception.ServiceUserNotFoundException;

public interface UserService {

    void register(UserDto userDto) throws ServiceUserAlreadyExistsException;

    void update(UserProfileDto userProfileDto) throws ServiceUserNotFoundException;

    void changePassword(ChangeUserPasswordDto passwordDto) throws ServiceUserNotFoundException, ServiceInvalidUserPasswordException;

    String setProfileImage(byte[] profileImage, int userId) throws ServiceProfileImageUploadException, ServiceUserNotFoundException;

    UserProfileDto getById(int userId) throws ServiceUserNotFoundException;
}
