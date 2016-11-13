package com.bitreight.tasklist.service.converter.impl;

import com.bitreight.tasklist.dto.UserDto;
import com.bitreight.tasklist.entity.User;
import com.bitreight.tasklist.service.converter.UserDtoConverter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverterImpl implements UserDtoConverter {

    @Override
    public User convertDto(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        return user;
    }

    @Override
    public UserDto convertEntity(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setName(user.getName());
        userDto.setSurname(user.getSurname());
        return userDto;
    }
}
