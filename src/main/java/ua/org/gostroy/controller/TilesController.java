package ua.org.gostroy.controller;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.org.gostroy.service.UserService;

/**
 * Created by panser on 5/23/14.
 */
@Controller
@RequestMapping("/tiles")
public class TilesController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired(required = true)
    private UserService userService;

    @Autowired
    JavaMailSender mailSender;
    @Autowired
    VelocityEngine velocityEngine;

    @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
    public String listUser(Model model){
        log.trace("listUser() start ...");
        model.addAttribute("users", userService.findAll());
//        return "userList";
        return "user/userList";
    }

}
