package com.bitreight.tasklist.controller;

import com.bitreight.tasklist.security.CustomUserDetails;
import com.bitreight.tasklist.dto.UserDto;
import com.bitreight.tasklist.service.UserService;
import com.bitreight.tasklist.service.exception.ServiceUserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public String goToRegistrationPage(Model model, @AuthenticationPrincipal CustomUserDetails user) {
        if(user != null) {
            return "redirect:/workspace";
        }

        model.addAttribute("userDto", new UserDto());
        return "registration";
    }

    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public String executeRegistration(@Valid UserDto userDto, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "registration";
        }

        try {
            userService.register(userDto);
        } catch (ServiceUserAlreadyExistsException e) {
            model.addAttribute("error", "Username already exists.");
            return "registration";
        }

        return "redirect:login";
    }
}