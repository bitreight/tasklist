package com.bitreight.tasklist.service;

import com.bitreight.tasklist.dao.UserDao;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedUserException;
import com.bitreight.tasklist.dto.UserDto;
import com.bitreight.tasklist.entity.User;
import com.bitreight.tasklist.service.converter.UserDtoConverter;
import com.bitreight.tasklist.service.converter.impl.UserDtoConverterImpl;
import com.bitreight.tasklist.service.exception.ServiceUserAlreadyExistsException;
import com.bitreight.tasklist.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestUserService {

    @Mock
    private UserDao mockUserDao;

    @Spy
    private UserDtoConverter spyUserConverter = new UserDtoConverterImpl();

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    private static User user = new User();
    private static User userToSave = new User();
    private static UserDto userDto = new UserDto();
    private static UserDto userDtoToRegister = new UserDto();

    @BeforeClass
    public static void setUpBeforeClass() {
        userDto.setId(1);
        userDto.setUsername("test_user");
        userDto.setPassword("pass");

        userDtoToRegister.setUsername("new_user");
        userDtoToRegister.setPassword("pass");

        user.setId(1);
        user.setUsername("test_user");
        user.setPassword("pass");

        userToSave.setUsername("new_user");
        userToSave.setPassword("pass");
    }

    @Test
    public void testRegisterUser() throws ServiceUserAlreadyExistsException,
            DaoSaveDuplicatedUserException {
        userService.register(userDtoToRegister);

        verify(spyUserConverter).convertDto(userDtoToRegister);
        verify(mockUserDao).save(userToSave);
    }

    @Test
    public void testRegisterUser_nullUserDto() throws ServiceUserAlreadyExistsException,
            DaoSaveDuplicatedUserException {
        userService.register(null);

        verify(spyUserConverter, never()).convertDto(any());
        verify(mockUserDao, never()).save(any());
    }

    @Test(expected = ServiceUserAlreadyExistsException.class)
    public void testRegisterUser_duplicatedUser() throws ServiceUserAlreadyExistsException,
            DaoSaveDuplicatedUserException {
        doThrow(DaoSaveDuplicatedUserException.class).when(mockUserDao).save(userToSave);

        userService.register(userDtoToRegister);

        verify(spyUserConverter).convertDto(userDtoToRegister);
        verify(mockUserDao).save(userToSave);
    }

    @Test
    public void testCheckCredentials() {
        when(mockUserDao.findByUsername(user.getUsername()))
                .thenReturn(user);

        UserDto newUserDto = userService.getByUsername(userDto.getUsername());

        assertEquals(newUserDto, userDto);
        verify(spyUserConverter).convertEntity(user);
        verify(mockUserDao).findByUsername(user.getUsername());
    }

    @Test
    public void testCheckCredentials_nullUserDto() {
        UserDto newUserDto = userService.getByUsername(null);

        assertNull(newUserDto);
        verify(spyUserConverter, never()).convertEntity(any());
        verify(mockUserDao, never()).findByUsername(anyString());
    }

    @Test
    public void testCheckCredentials_nonExistentUser() {
        when(mockUserDao.findByUsername(user.getUsername()))
                .thenReturn(null);

        UserDto newUserDto = userService.getByUsername(userDto.getUsername());

        assertNull(newUserDto);
        verify(spyUserConverter, never()).convertEntity(user);
        verify(mockUserDao).findByUsername(user.getUsername());
    }

    @Test
    public void testUpdateUser() {
        userService.update(userDto);

        verify(spyUserConverter).convertDto(userDto);
        verify(mockUserDao).update(user);
    }

    @Test
    public void testUpdateUser_nullUserDto() {
        userService.update(null);

        verify(spyUserConverter, never()).convertDto(any());
        verify(mockUserDao, never()).update(any());
    }

    @Test
    public void testGetInfoById() {
        when(mockUserDao.findById(1)).thenReturn(user);

        UserDto newUserDto = userService.getInfoById(userDto.getId());

        assertEquals(newUserDto, userDto);
        verify(spyUserConverter).convertEntity(user);
        verify(mockUserDao).findById(user.getId());
    }

    @Test
    public void testGetInfoById_zeroUserId() {
        UserDto newUserDto = userService.getInfoById(0);

        assertNull(newUserDto);
        verify(spyUserConverter, never()).convertEntity(any());
        verify(mockUserDao, never()).findById(anyInt());
    }

    @Test
    public void testGetInfoById_nonExistentUser() {
        when(mockUserDao.findById(1)).thenReturn(null);

        UserDto newUserDto = userService.getInfoById(1);

        assertNull(newUserDto);
        verify(spyUserConverter, never()).convertEntity(any());
        verify(mockUserDao).findById(1);
    }
}