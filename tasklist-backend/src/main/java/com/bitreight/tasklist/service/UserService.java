package com.bitreight.tasklist.service;

import com.bitreight.tasklist.dto.UserDto;

public interface UserService {

    void register(UserDto userDto);

    UserDto checkCredentials(UserDto userDto);

    void update(UserDto userDto);

    UserDto getInfoById(int userId);
}
