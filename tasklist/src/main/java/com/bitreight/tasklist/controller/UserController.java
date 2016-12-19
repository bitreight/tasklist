package com.bitreight.tasklist.controller;

import com.bitreight.tasklist.dto.UserDto;
import com.bitreight.tasklist.security.CustomUserDetails;
import com.bitreight.tasklist.service.UserService;
import com.bitreight.tasklist.service.exception.ServiceUserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


}
