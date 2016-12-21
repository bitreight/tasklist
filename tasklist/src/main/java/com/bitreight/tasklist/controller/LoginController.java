package com.bitreight.tasklist.controller;

import com.bitreight.tasklist.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage(Model model, @AuthenticationPrincipal CustomUserDetails user) {
        if(user != null) {
            return "redirect:/workspace";
        }

        model.addAttribute("loginError", "Invalid username or password.");
        return "login";
    }
}