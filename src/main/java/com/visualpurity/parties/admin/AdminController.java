package com.visualpurity.parties.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdminController {

    @ResponseBody
    @GetMapping("/admin")
    public Object hello() {
        return "Hello";
    }

    @RequestMapping(value = "/admin/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String login() {
        return "login";
    }

}
