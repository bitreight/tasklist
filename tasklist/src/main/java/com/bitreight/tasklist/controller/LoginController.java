package com.bitreight.tasklist.controller;

import com.bitreight.tasklist.dto.UserDto;
import com.bitreight.tasklist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String goToLoginPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String executeLogin(@Valid UserDto userDto, BindingResult result) {
        if(result.hasErrors()) {
            return "login";
        }

        UserDto validUser = userService.checkCredentials(userDto);
        if(validUser != null) {
            System.out.println("Succesfully logged in.\n" + validUser);
        } else {
            System.out.println("Invalid credentials.");
        }

        return "login";
    }
//    @RequestMapping(value = "/logout", method = RequestMethod.GET)
//    public String executeLogout() {
//        return "login";
//    }

}