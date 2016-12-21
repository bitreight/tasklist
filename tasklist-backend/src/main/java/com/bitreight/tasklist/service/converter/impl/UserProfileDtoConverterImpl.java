package com.bitreight.tasklist.service.converter.impl;

import com.bitreight.tasklist.dto.UserProfileDto;
import com.bitreight.tasklist.entity.User;
import com.bitreight.tasklist.service.converter.UserProfileDtoConverter;
import org.springframework.stereotype.Component;

@Component
public class UserProfileDtoConverterImpl implements UserProfileDtoConverter {

    @Override
    public User convertDto(UserProfileDto userProfileDto) {
        User user = new User();
        user.setId(userProfileDto.getId());
        user.setUsername(userProfileDto.getUsername());
        user.setName(userProfileDto.getName());
        user.setSurname(userProfileDto.getSurname());
        user.setProfileImagePath(userProfileDto.getProfileImagePath());
        return user;
    }

    @Override
    public UserProfileDto convertEntity(User user) {
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setId(user.getId());
        userProfileDto.setUsername(user.getUsername());
        userProfileDto.setName(user.getName());
        userProfileDto.setSurname(user.getSurname());
        userProfileDto.setProfileImagePath(user.getProfileImagePath());
        return userProfileDto;
    }
}
