package ua.org.gostroy.controller;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.org.gostroy.entity.User;
import ua.org.gostroy.service.UserService;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
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
    @RequestMapping(value = {"/{login}"}, method = RequestMethod.GET)
    public String editUser(Model model, @PathVariable String login){
        model.addAttribute("user", userService.findByLogin(login));
        return "/user/userEdit";
    }
    @PreAuthorize("#userFromForm.login == authentication.name or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = {"/{login}"}, method = RequestMethod.PUT)
    public String editUser(Model model, @ModelAttribute("user") User userFromForm, BindingResult userFromFormError,
                           @PathVariable(value = "login") String login
                           ) throws IOException{
        String viewName;
        log.debug("editUser(), userFromForm.login = " + login);
        if(userFromFormError.hasErrors()){
            model.addAttribute("userFromFormError", userFromFormError);
            viewName = "/user/userEdit";
        }
        else{
//            userService.save(userFromForm);
            User user = userService.findByLogin(login);
            user.setEmail(userFromForm.getEmail());
            user.setPassword(userFromForm.getPassword());
            if(userFromForm.getAvatarImage()!=null){
                user.setAvatarImage(userFromForm.getAvatarImage());
            }
            user.setBirthDay(userFromForm.getBirthDay());
            log.trace("editUser(), user = " + user);
            userService.save(user);
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
    public String registerPOST(@Valid @ModelAttribute("user") User userFromForm, BindingResult result) throws MessagingException {
        String viewName;
        if(result.hasErrors()){
            viewName = "/user/userAdd";
        }
        else
        {
            userFromForm.setEnabled(true);
            userService.save(userFromForm);
            viewName = "/auth/login";
        }
//        this.sendEmailOfRegistration(user);
        return viewName;
    }

    @RequestMapping(value = {"/{id}/delete"}, method = RequestMethod.GET)
    public String deleteUser(Model model, @PathVariable String id){
        User deleteUser = userService.find(Long.parseLong(id));
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
            Resource resource = new ClassPathResource("/template/image/avatar.jpg");
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
        helper.setTo("gostroy@gmail.com");
        helper.setSubject("Account data");

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("login", user.getLogin());
        model.put("password", user.getPassword());
        String emailText = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/emailRegisterTemplate.vm", "UTF-8", model);

        helper.setText(emailText, true);
        ClassPathResource image = new ClassPathResource("templates/avator_default.jpg");
        helper.addInline("accountLogo", image); // Встроенное изображение
        mailSender.send(message);
    }
}
