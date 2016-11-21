package com.bitreight.tasklist.converter;

import com.bitreight.tasklist.dto.UserDto;
import com.bitreight.tasklist.entity.User;
import com.bitreight.tasklist.service.converter.UserDtoConverter;
import com.bitreight.tasklist.service.converter.impl.UserDtoConverterImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestUserDtoConverter {

    private UserDtoConverter userConverter = new UserDtoConverterImpl();

    private static UserDto userDto = new UserDto();
    private static User user = new User();

    private static List<User> users = new ArrayList<>(1);
    private static List<UserDto> userDtos = new ArrayList<>(1);

    @BeforeClass
    public static void setUpBeforeClass() {
        userDto.setId(1);
        userDto.setUsername("test_user");
        userDto.setPassword("test_password");
        userDto.setName("test_name");
        userDto.setSurname("test_surname");

        user.setId(1);
        user.setUsername("test_user");
        user.setPassword("test_password");
        user.setName("test_name");
        user.setSurname("test_surname");

        users.add(user);
        userDtos.add(userDto);
    }

    @Test
    public void testConvertUserDto() {
        User actualUser = userConverter.convertDto(userDto);
        assertEquals(actualUser, user);
    }

    @Test
    public void testConvertUserEntity() {
        UserDto actualUserDto = userConverter.convertEntity(user);
        assertEquals(actualUserDto, userDto);
    }

    @Test
    public void testConvertUserDtos() {
        List<User> actualUsers = userConverter.convertDtos(userDtos);
        assertEquals(actualUsers, users);
    }

    @Test
    public void testConvertUserEntities() {
        List<UserDto> actualUserDtos = userConverter.convertEntities(users);
        assertEquals(actualUserDtos, userDtos);
    }

    @Test
    public void testConvertUserDtos_emptyList() {
        List<User> actualUsers = userConverter.convertDtos(new ArrayList<>(0));
        assertTrue(actualUsers.isEmpty());
    }

    @Test
    public void testConvertUserEntities_emptyList() {
        List<UserDto> actualUserDtos = userConverter.convertEntities(new ArrayList<>(0));
        assertTrue(actualUserDtos.isEmpty());
    }
}
