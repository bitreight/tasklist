package com.bitreight.tasklist.service;

import com.bitreight.tasklist.dto.UserDto;
import com.bitreight.tasklist.service.exception.ServiceUserAlreadyExistsException;
import com.bitreight.tasklist.service.exception.ServiceUserNotFoundException;

public interface UserService {

    void register(UserDto userDto) throws ServiceUserAlreadyExistsException;

    void update(UserDto userDto) throws ServiceUserNotFoundException;

    UserDto getById(int userId) throws ServiceUserNotFoundException;
}
