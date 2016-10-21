package com.bitreight.tasklist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/home")
    public String goHome(Model model) {

        model.addAttribute("greeting", "Welcome home!");
        return "home";
    }
}
