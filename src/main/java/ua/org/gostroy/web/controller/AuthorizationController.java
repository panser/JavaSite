package ua.org.gostroy.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.org.gostroy.service.UserService;
import ua.org.gostroy.web.form.Message;

import java.util.Locale;

/**
 * Created by panser on 5/22/14.
 */
@Controller
@RequestMapping("/auth")
public class AuthorizationController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired(required = true)
    private UserService userService;
    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String login(Model model, Locale locale, @RequestParam(required = false) String login_error){
        if(login_error != null){
            model.addAttribute("message_error", new Message("error", messageSource.getMessage("message_login_fail", new Object[]{}, locale)));
        }
        return "/auth/login";
    }

}

