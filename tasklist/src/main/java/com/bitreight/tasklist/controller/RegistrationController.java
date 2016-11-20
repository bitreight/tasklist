package com.bitreight.tasklist.controller;

import com.bitreight.tasklist.dto.UserDto;
import com.bitreight.tasklist.service.UserService;
import com.bitreight.tasklist.service.exception.ServiceUserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/join", method = RequestMethod.GET)
    public String goToRegistrationPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "registration";
    }

    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public String executeRegistration(@Valid UserDto userDto, BindingResult result) {
        if(result.hasErrors()) {
            return "registration";
        }

        try {
            userService.register(userDto);
        } catch (ServiceUserAlreadyExistsException e) {
            //error message on page
            return "registration";
        }

        return "redirect:/login";
    }
}
