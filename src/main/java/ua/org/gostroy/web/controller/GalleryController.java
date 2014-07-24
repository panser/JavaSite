package ua.org.gostroy.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.org.gostroy.service.ImageService;

/**
 * Created by panser on 6/18/2014.
 */
@Controller
@RequestMapping("/gallery")
public class GalleryController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired(required = true)
    private ImageService imageService;

    @RequestMapping(method = RequestMethod.GET)
    public String listImagesGET(Model model, @RequestParam(required = false) Integer page){
        log.trace("listImagesGET(), page: " + page);
        page = (page == null) ? 0 : page;
        Integer number = 40;
        model.addAttribute("images", imageService.findByPage(page, number));
        model.addAttribute("page", page);
        return "/gallery/imageAll";
    }
}
