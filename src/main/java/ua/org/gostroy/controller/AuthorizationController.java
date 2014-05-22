package ua.org.gostroy.controller;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.org.gostroy.service.UserService;

/**
 * Created by panser on 5/22/14.
 */
@Controller
@RequestMapping("/auth")
public class AuthorizationController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired(required = true)
    private UserService userService;

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String login(){
        return "/auth/login";
    }

}

