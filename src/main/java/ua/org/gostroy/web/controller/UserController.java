package ua.org.gostroy.web.controller;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.org.gostroy.domain.User;
import ua.org.gostroy.exception.EntityNotFound;
import ua.org.gostroy.service.UserService;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by panser on 5/22/14.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired(required = true)
    private UserService userService;

    @Autowired
    JavaMailSender mailSender;
    @Autowired
    VelocityEngine velocityEngine;

    @Autowired
    AuthenticationManager authenticationManager;

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String listUser(Model model){
        log.trace("listUser() start ...");
        model.addAttribute("users", userService.findAll());
        return "/user/userList";
    }

/*
    @InitBinder
    @RequestMapping(value = {"/edit/{login}"})
    protected void initBinder(WebDataBinder binder){
        binder.setAllowedFields("id","version","login","email","password","avatarImage","birthDay");
    }
*/
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = {"add"}, method = RequestMethod.GET)
    public String addUser(Model model){
        model.addAttribute("user", new User());
        return "/user/userEdit";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = {"add"}, method = RequestMethod.PUT)
    public String addUser(Model model, @Valid @ModelAttribute("user") User userFromForm, BindingResult userFromFormError) throws IOException{
        String viewName;
        log.debug("addUser()");
        if(userFromFormError.hasErrors()){
            viewName = "/user/userEdit";
        }
        else{
//            userService.save(userFromForm);
            User user = new User();
            user.setLogin(userFromForm.getLogin());
            user.setEmail(userFromForm.getEmail());
            user.setPassword(userFromForm.getPassword());
            if(userFromForm.getAvatarImage()!=null){
                user.setAvatarImage(userFromForm.getAvatarImage());
            }
            user.setBirthDay(userFromForm.getBirthDay());
            user.setReceiveNewsletter(userFromForm.getReceiveNewsletter());
            user.setEnabled(userFromForm.getEnabled());
            user.setSex(userFromForm.getSex());
            user.setRole(userFromForm.getRole());
            log.trace("addUser(), user = " + user);
            userService.create(user);
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
    public String editUser(Model model, @Valid @ModelAttribute("user") User userFromForm, BindingResult userFromFormError,
                           @PathVariable(value = "login") String login
                           ) throws IOException{
        String viewName;
        log.debug("editUser(), userFromForm.login = " + login);
        if(userFromFormError.hasErrors()){
            viewName = "/user/userEdit";
        }
        else{
//            userService.save(userFromForm);
            User user = userService.findByLogin(login);
            user.setLogin(userFromForm.getLogin());
            user.setEmail(userFromForm.getEmail());
            user.setPassword(userFromForm.getPassword());
            if(userFromForm.getAvatarImage()!=null){
                user.setAvatarImage(userFromForm.getAvatarImage());
            }
            user.setBirthDay(userFromForm.getBirthDay());
            user.setReceiveNewsletter(userFromForm.getReceiveNewsletter());
            user.setEnabled(userFromForm.getEnabled());
            user.setSex(userFromForm.getSex());
            user.setRole(userFromForm.getRole());
            log.trace("editUser(), user = " + user);
            userService.update(user);
            viewName = "redirect:/user/";
        }
        return viewName;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerGET(Model model){
        model.addAttribute("user", new User());
        return "/user/userAdd";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPOST(@Valid @ModelAttribute("user") User userFromForm, BindingResult result,
                               HttpServletRequest request)
            throws MessagingException, URISyntaxException {
        String viewName;
        if(result.hasErrors()){
            viewName = "/user/userAdd";
        }
        else
        {
            String randomString = RandomStringUtils.random(30,true,true);
            String requestURL = request.getRequestURL().toString();
            requestURL = requestURL.replaceAll("register","");
            requestURL = requestURL + "confirm/" + randomString;
//            log.trace("registerPOST(), requestURL: " + requestURL);
            userFromForm.setRegUrI(requestURL);
            log.trace("registerPOST(), userFromForm: " + userFromForm);
            userService.create(userFromForm);
            sendEmailOfRegistration(userFromForm);
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

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword());
        Authentication authenticationResult = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationResult);

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
        BufferedImage bufferedImage;
        if(user.getAvatarImage() != null && user.getAvatarImage().length != 0){
            bufferedImage = ImageIO.read(new ByteArrayInputStream(user.getAvatarImage()));
        }
        else{
            Resource resource = new ClassPathResource("/templates/image/avatar.jpg");
            bufferedImage = ImageIO.read(resource.getFile());
        }
        Graphics g = bufferedImage.getGraphics();
        g.drawString("gostroy.org.ua",20,20);
        return bufferedImage;
    }

    public void sendEmailOfRegistration(User user) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("noreply@gostroy.org.ua");
        helper.setTo(user.getEmail());
        helper.setSubject("Account confirm");

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("login", user.getLogin());
        model.put("password", user.getPassword());
        model.put("regURI", user.getRegUrI());
        String emailText = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/mail/emailRegisterTemplate.vm", "UTF-8", model);

        helper.setText(emailText, true);
        ClassPathResource image = new ClassPathResource("templates/image/site_logo.jpg");
        helper.addInline("siteLogo", image); // Встроенное изображение
        mailSender.send(message);
    }


    @ModelAttribute("roleList")
    public Map<String,String> populateRoleList() {
        Map<String,String> role = new LinkedHashMap<String,String>();
        role.put("ROLE_ADMIN", "admin");
        role.put("ROLE_USER", "user");
        return role;
    }

}
