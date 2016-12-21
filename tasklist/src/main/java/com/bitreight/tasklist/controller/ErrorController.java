package com.bitreight.tasklist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ErrorController {

    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public String get404ErrorPage() {
        return "404";
    }
}
