package com.bitreight.tasklist.service;

import com.bitreight.tasklist.config.DaoMockConfiguration;
import com.bitreight.tasklist.config.ServiceContextConfiguration;
import com.bitreight.tasklist.dao.UserDao;
import com.bitreight.tasklist.dto.UserDto;
import com.bitreight.tasklist.entity.User;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ServiceContextConfiguration.class, DaoMockConfiguration.class },
        loader = AnnotationConfigContextLoader.class)
public class TestUserService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao mockUserDao;

    private static UserDto userDto = new UserDto();
    private static UserDto userDtoToAdd = new UserDto();

    private static User user = new User();
    private static User userToAdd = new User();

    @BeforeClass
    public static void setUpBeforeClass() {
        userDto.setId(1);
        userDto.setUsername("test_user");
        userDto.setPassword("pass");

        userDtoToAdd.setUsername("test_user");
        userDtoToAdd.setPassword("pass");

        user.setId(1);
        user.setUsername("test_user");
        user.setPassword("pass");

        userToAdd.setUsername("test_user");
        userToAdd.setPassword("pass");
    }

    @Before
    public void setUp() {
        when(mockUserDao.findById(user.getId()))
                .thenReturn(user);
        when(mockUserDao.findByUsernameAndPassword(user.getUsername(), user.getPassword()))
                .thenReturn(user);
    }

    @After
    public void tearDown() {
        reset(mockUserDao);
    }

    @Test
    public void testRegister() {
        userService.register(userDtoToAdd);
        verify(mockUserDao, times(1)).save(userToAdd);
    }

    @Test
    public void testRegister_nullUserDto() {
        userService.register(null);
        verify(mockUserDao, never()).save(any());
    }

    @Test
    public void testCheckCredentials() {
        UserDto newUserDto = userService.checkCredentials(userDto);
        assertEquals(newUserDto, userDto);
        verify(mockUserDao, times(1)).findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    @Test
    public void testCheckCredentials_nullUserDto() {
        UserDto newUserDto = userService.checkCredentials(null);
        assertNull(newUserDto);
        verify(mockUserDao, never()).findByUsernameAndPassword(anyString(), anyString());
    }

    @Test
    public void testUpdate() {
        userService.update(userDto);
        verify(mockUserDao, times(1)).update(user);
    }

    @Test
    public void testUpdate_nullUserDto() {
        userService.update(null);
        verify(mockUserDao, never()).update(any());
    }

    @Test
    public void testGetInfoById() {
        UserDto newUserDto = userService.getInfoById(userDto.getId());
        assertEquals(newUserDto, userDto);
        verify(mockUserDao, times(1)).findById(user.getId());
    }

    @Test
    public void testGetInfoById_zeroUserId() {
        UserDto newUserDto = userService.getInfoById(0);
        assertNull(newUserDto);
        verify(mockUserDao, never()).findById(anyInt());
    }
}