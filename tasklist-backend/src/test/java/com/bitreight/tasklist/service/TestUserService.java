package com.bitreight.tasklist.service;

import com.bitreight.tasklist.dao.UserDao;
import com.bitreight.tasklist.dao.exception.DaoSaveDuplicatedUserException;
import com.bitreight.tasklist.dto.UserDto;
import com.bitreight.tasklist.entity.User;
import com.bitreight.tasklist.service.converter.UserDtoConverter;
import com.bitreight.tasklist.service.converter.impl.UserDtoConverterImpl;
import com.bitreight.tasklist.service.exception.ServiceUserAlreadyExistsException;
import com.bitreight.tasklist.service.impl.UserServiceImpl;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@Ignore
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

    @Test(expected = IllegalArgumentException.class)
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

//    @Test
//    public void testFindByUsername() throws ServiceUserNotFoundException {
//        when(mockUserDao.findByUsername(user.getUsername()))
//                .thenReturn(user);
//
//        UserDto actualUserDto = userService.getByUsername(userDto.getUsername());
//
//        assertEquals(actualUserDto, userDto);
//        verify(spyUserConverter).convertEntity(user);
//        verify(mockUserDao).findByUsername(user.getUsername());
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testFindByUsername_nullUserDto() throws ServiceUserNotFoundException {
//        UserDto actualUserDto = userService.getByUsername(null);
//
//        verify(spyUserConverter, never()).convertEntity(any());
//        verify(mockUserDao, never()).findByUsername(anyString());
//    }
//
//    @Test(expected = ServiceUserNotFoundException.class)
//    public void testFindByUsername_nonExistentUser() throws ServiceUserNotFoundException {
//        when(mockUserDao.findByUsername(user.getUsername()))
//                .thenReturn(null);
//
//        UserDto actualUserDto = userService.getByUsername(userDto.getUsername());
//
//        verify(spyUserConverter, never()).convertEntity(user);
//        verify(mockUserDao).findByUsername(user.getUsername());
//    }

//    @Test
//    public void testUpdateUser() throws ServiceUserNotFoundException {
//        when(mockUserDao.findById(1)).thenReturn(user);
//
//        userService.update(userDto);
//
//        verify(spyUserConverter).convertDto(userDto);
//        verify(mockUserDao).findById(1);
//        verify(mockUserDao).update(user);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testUpdateUser_nullUserDto() throws ServiceUserNotFoundException {
//        userService.update(null);
//
//        verify(spyUserConverter, never()).convertDto(any());
//        verify(mockUserDao, never()).findById(anyInt());
//        verify(mockUserDao, never()).update(any());
//    }
//
//    @Test(expected = ServiceUserNotFoundException.class)
//    public void testUpdateUser_nonExistentUer() throws ServiceUserNotFoundException {
//        when(mockUserDao.findById(1)).thenReturn(null);
//
//        userService.update(userDto);
//
//        verify(spyUserConverter, never()).convertDto(any());
//        verify(mockUserDao).findById(1);
//        verify(mockUserDao, never()).update(any());
//    }
//
//    @Test
//    public void testGetInfoById() throws ServiceUserNotFoundException {
//        when(mockUserDao.findById(1)).thenReturn(user);
//
//        UserDto newUserDto = userService.getById(userDto.getId());
//
//        assertEquals(newUserDto, userDto);
//        verify(spyUserConverter).convertEntity(user);
//        verify(mockUserDao).findById(user.getId());
//    }
//
//    @Test(expected = ServiceUserNotFoundException.class)
//    public void testGetInfoById_nonExistentUser() throws ServiceUserNotFoundException {
//        when(mockUserDao.findById(1)).thenReturn(null);
//
//        UserDto newUserDto = userService.getById(1);
//
//        verify(spyUserConverter, never()).convertEntity(any());
//        verify(mockUserDao).findById(1);
//    }
}