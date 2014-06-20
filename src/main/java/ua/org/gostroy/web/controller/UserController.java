package ua.org.gostroy.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.org.gostroy.exception.EntityNotFound;
import ua.org.gostroy.model.User;
import ua.org.gostroy.service.UserControllerService;
import ua.org.gostroy.service.UserService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by panser on 5/22/14.
 */
@Controller
@RequestMapping("/user")
@SessionAttributes("user")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired(required = true)
    private UserService userService;
    @Autowired(required = true)
    private UserControllerService userControllerService;

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String listUser(Model model){
        log.trace("listUser() start ...");
        model.addAttribute("users", userService.findAll());
        return "/user/userList";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = {"add"}, method = RequestMethod.GET)
    public String addUser(Model model){
        model.addAttribute("user", new User());
        return "/user/userEdit";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = {"add"}, method = RequestMethod.POST)
    public String addUser(Model model, @Valid User userFromForm, BindingResult userFromFormError, RedirectAttributes redirectAttributes)
            throws IOException{
        String viewName;
        log.debug("addUser()");
        validateUserOnUnique(userFromForm, userFromFormError);
        if(userFromFormError.hasErrors()){
            viewName = "/user/userEdit";
        }
        else{
            userService.create(userFromForm);
            redirectAttributes.addFlashAttribute("message", "User add successfully");
            viewName = "redirect:/user/";
        }
        return viewName;
    }


    @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = {"{login}/profile"}, method = RequestMethod.GET)
    public String editUser(Model model, @PathVariable String login){
        model.addAttribute("user", userService.findByLogin(login));
        return "/user/userEdit";
    }
    @RequestMapping(value = {"{login}/profile"}, method = RequestMethod.PUT)
    public String editUser(Model model, @Valid User user, BindingResult userFromFormError,
                           @PathVariable String login, SessionStatus sessionStatus,
                           RedirectAttributes redirectAttributes) throws IOException{
        String viewName;
        log.debug("editUser(), userFromForm.login = " + login);
        validateUserOnUnique(user, userFromFormError);
        if(userFromFormError.hasErrors()){
            viewName = "/user/userEdit";
        }
        else{
            log.trace("editUser(), userFromForm = " + user);
            userService.update(user);
            sessionStatus.setComplete();
            redirectAttributes.addFlashAttribute("message", "Profile save successfully");
            viewName = "redirect:/";
        }
        return viewName;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerGET(Model model){
        model.addAttribute("user", new User());
        return "/user/register";
    }
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPOST(@Valid User userFromForm, BindingResult result, HttpServletRequest request)
            throws MessagingException, URISyntaxException {
        String viewName;
        validateUserOnUnique(userFromForm, result);
        if(result.hasErrors()){
            viewName = "/user/register";
        }
        else
        {
//            log.trace("registerPOST(), requestURL: " + requestURL);
            String regUrl = userControllerService.generateRegistrationUrl(request.getRequestURL().toString());
            userFromForm.setRegUrI(regUrl);
            log.trace("registerPOST(), userFromForm: " + userFromForm);
            userService.create(userFromForm);
            userControllerService.sendEmailOfRegistration(userFromForm);
            viewName = "redirect:/";
        }
        return viewName;
    }
    @RequestMapping(value = "/confirm/{randomString}", method = RequestMethod.GET)
    public String confirmRegister(HttpServletRequest request,RedirectAttributes redirectAttributes) throws EntityNotFound {
        String requestURL = request.getRequestURL().toString();
        User user = userService.findByRegUrI(requestURL);
        if(user == null){
            throw new EntityNotFound("Entity:User not found to confirm registration");
        }

        user.setEnabled(true);
        user.setRegUrI("!" + user.getRegUrI());
        userService.create(user);
        userControllerService.setAuthenticationContext(user);

        redirectAttributes.addFlashAttribute("confirmRegistration", "Congratulation! We confirm your registration.");
        return "redirect:/user/" + user.getLogin() + "/profile";
//        return "/user/confirmRegistration";
    }

    @PreAuthorize("#login == authentication.name or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = {"/{login}/delete"}, method = RequestMethod.GET)
    public String deleteUser(Model model, @PathVariable String login){
        User deleteUser = userService.findByLogin(login);
        userService.delete(deleteUser);
        return "redirect:/user/";
    }


    @RequestMapping(value = "/{login}/avatar", method = RequestMethod.GET)
    @ResponseBody
    public BufferedImage getAvatar(@PathVariable String login) throws IOException{
        User user = userService.findByLogin(login);
        return userControllerService.getAvator(user);
    }



    @ModelAttribute("roleList")
    public Map<String,String> populateRoleList() {
        Map<String,String> role = new LinkedHashMap<String,String>();
        role.put("ROLE_ADMIN", "admin");
        role.put("ROLE_USER", "user");
        return role;
    }


//    vallidation on unique
    public void validateUserOnUnique(User user, Errors errors){
        Object checkByLogin = userService.findByLogin(user.getLogin());
        if(checkByLogin != null){
            errors.rejectValue("login", "validation.user.login.Duplicate", "already exists");
        }
    }


}
