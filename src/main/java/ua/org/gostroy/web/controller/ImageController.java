package ua.org.gostroy.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.org.gostroy.domain.Image;
import ua.org.gostroy.domain.User;
import ua.org.gostroy.service.ImageService;
import ua.org.gostroy.service.UserService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Created by panser on 6/2/2014.
 */
@Controller
@RequestMapping("/{login}/image")
public class ImageController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired(required = true)
    private ImageService imageService;
    @Autowired(required = true)
    private UserService userService;

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String listImages(Model model, @PathVariable String login){
        User user = userService.findByLogin(login);
        model.addAttribute("images", imageService.findByUser(user));
        return "/image/imageList";
    }

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
    @ResponseBody
    public BufferedImage viewImage(Model model, @PathVariable String id) throws IOException {
        Image image = imageService.find(Long.parseLong(id));
//        Resource resource = new ClassPathResource("/templates/image/avatar.jpg");
//        BufferedImage bufferedImage = ImageIO.read(resource.getFile());
        BufferedImage bufferedImage = ImageIO.read(new URL("http","localhost",8082,image.getPath()));
        Graphics g = bufferedImage.getGraphics();
        g.drawString("gostroy.org.ua",20,20);
        return bufferedImage;

    }
}
