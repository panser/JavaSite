package ua.org.gostroy.controller;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.org.gostroy.entity.User;
import ua.org.gostroy.service.UserService;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setBindEmptyMultipartFiles(false);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
    public String listUser(Model model){
        log.trace("listUser() start ...");
        model.addAttribute("users", userService.findAll());
        return "user/userList";
    }

    @RequestMapping(value = {"/edit/{login}"}, method = RequestMethod.GET)
    public String editUser(Model model, @PathVariable String login){
        model.addAttribute("user", userService.findByLogin(login));
        return "user/userEdit";
    }
    @RequestMapping(value = {"/edit"}, method = RequestMethod.GET)
    public String editUserDef(Model model){
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        log.trace("editUserDef: principal = " + principal);
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return "redirect:/user/edit/" + login;
    }
    @RequestMapping(value = {"/edit/{login}"}, method = RequestMethod.PUT)
    public String editUser(Model model, @ModelAttribute("user") User userFromForm, BindingResult userFromFormError,
                           @PathVariable(value = "login") String login,
                           @RequestParam(value = "avatarImage", required = false)MultipartFile avatarImage) throws IOException{
        String viewName;
//        if(userFromFormError.hasErrors()){
//            log.debug("ERROR!!!!!!!!!!!!!!!!!!!\n\n\n");
//            model.addAttribute("userFromFormError", userFromFormError);
//            viewName = "user/userEdit";
//        }
//        else{
            log.trace("RECEIVE IMAGE: " + avatarImage.getOriginalFilename());
            User user = userService.findByLogin(login);
            log.trace("RECEIVED user object from EditForm: " + userFromForm);
            user.setLogin(userFromForm.getLogin());
            user.setEmail(userFromForm.getEmail());
            user.setPassword(userFromForm.getPassword());
            user.setBirthDay(userFromForm.getBirthDay());
            if (!avatarImage.isEmpty()) {
                byte[] bytes = avatarImage.getBytes();
                user.setAvatarImage(bytes);
            }
            log.trace("SAVED user object: " + user);

            userService.save(user);
            viewName = "redirect:/user/list";
//        }
        return viewName;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerGET(Model model){
        model.addAttribute("user", new User());
        return "user/userAdd";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPOST(@Valid @ModelAttribute("user") User userFromForm, BindingResult result) throws MessagingException {
        User user = new User();
        user.setLogin(userFromForm.getLogin());
        user.setPassword(userFromForm.getPassword());
        user.setEnabled(true);

        String viewName;
        if(result.hasErrors()){
            viewName = "/user/userAdd";
        }
        else
        {
            userService.save(user);
            viewName = "/auth/login";
        }
//        this.sendEmailOfRegistration(user);
        return viewName;
    }

    @RequestMapping(value = "/avatar/{login}", method = RequestMethod.GET)
    @ResponseBody
    public BufferedImage getAvatar(@PathVariable String login) throws IOException{
        User user = userService.findByLogin(login);
//        log.trace("getAvatar(), user = " + user);
        BufferedImage bufferedImage;
        if(user.getAvatarImage()!=null){
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
