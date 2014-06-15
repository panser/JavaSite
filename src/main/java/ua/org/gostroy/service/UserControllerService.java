package ua.org.gostroy.service;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import ua.org.gostroy.model.User;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by panser on 6/15/2014.
 */
@Service
public class UserControllerService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    JavaMailSender mailSender;
    @Autowired
    VelocityEngine velocityEngine;

    @Autowired
    AuthenticationManager authenticationManager;

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

    public void setAuthenticationContext(User user){
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword());
        Authentication authenticationResult = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationResult);
    }

    public String generateRegistrationUrl(String requestURL){
        String randomString = RandomStringUtils.random(30, true, true);
        requestURL = requestURL.replaceAll("register","");
        requestURL = requestURL + "confirm/" + randomString;
        return requestURL;
    }

    public BufferedImage getAvator(User user) throws IOException {
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
}
