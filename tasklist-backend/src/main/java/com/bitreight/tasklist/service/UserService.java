package com.bitreight.tasklist.service;

import com.bitreight.tasklist.dto.UserDto;
import com.bitreight.tasklist.service.exception.ServiceUserAlreadyExistsException;

public interface UserService {

    void register(UserDto userDto) throws ServiceUserAlreadyExistsException;

    UserDto checkCredentials(UserDto userDto);

    void update(UserDto userDto);

    UserDto getInfoById(int userId);
}
