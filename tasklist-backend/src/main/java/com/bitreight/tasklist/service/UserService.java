package com.bitreight.tasklist.service;

import com.bitreight.tasklist.dto.ChangePasswordDto;
import com.bitreight.tasklist.dto.UserDto;
import com.bitreight.tasklist.service.exception.ServiceInvalidUserPasswordException;
import com.bitreight.tasklist.service.exception.ServiceUserAlreadyExistsException;
import com.bitreight.tasklist.service.exception.ServiceUserNotFoundException;

public interface UserService {

    void register(UserDto userDto) throws ServiceUserAlreadyExistsException;

    void update(UserDto userDto) throws ServiceUserNotFoundException;

    void changePassword(ChangePasswordDto passwordDto) throws ServiceUserNotFoundException, ServiceInvalidUserPasswordException;

    UserDto getById(int userId) throws ServiceUserNotFoundException;
}
