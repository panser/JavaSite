package ua.org.gostroy.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.org.gostroy.model.User;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by panser on 5/30/2014.
 */
@Controller
@RequestMapping("/test")
public class TestController {
    //    for test bean Life Circle
    @PostConstruct
    protected void beforeConstruct(){
        System.out.println(getClass() + " :post construct method invoked");
    }
    @PreDestroy
    protected void beforeDestroy() {
        System.out.println(getClass() + "before destroy method invoked");
    }
    //    for test bean Life Circle


    @RequestMapping(method = RequestMethod.GET)
    public String test(ModelMap model) {
        model.addAttribute("message", "Hello world!");
        return "test";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/security", method = RequestMethod.GET)
    public String testSecurity(ModelMap model) {
        model.addAttribute("message", "Hello world!");
        return "test";
    }

    @ModelAttribute
    public void populateModel(Model model){
        model.addAttribute("user", new User());
    }

    @RequestMapping(value = {"/{viewName}"}, method = RequestMethod.GET)
    public String test(ModelMap model, @PathVariable String viewName) {
        return "/test/" + viewName;
    }

}
