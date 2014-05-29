package ua.org.gostroy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by panser on 5/25/2014.
 */
@Controller
@RequestMapping("/")
public class RedirectController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping
    public String root(HttpServletRequest request){
        return "redirect:/article/";
    }

    @RequestMapping(value = {"article"})
    public String redirectArticle(){
        log.trace("redirectArticle() start ...");
        return "redirect:/article/";
    }

    @RequestMapping(value = {"user"})
    public String redirectUser(){
        log.trace("redirectUser() start ...");
        return "redirect:/user/";
    }

}
