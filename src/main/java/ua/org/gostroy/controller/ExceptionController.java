package ua.org.gostroy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by panser on 5/23/14.
 */
@Controller
@RequestMapping(value = "/exception")
public class ExceptionController {

    @RequestMapping(value = "/optimisticLocking")
    public String optimisticLocking(){
        return "/exception/optimisticLocking";
    }
}
