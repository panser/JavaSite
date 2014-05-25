package ua.org.gostroy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.org.gostroy.entity.Article;
import ua.org.gostroy.entity.User;
import ua.org.gostroy.service.ArticleService;
import ua.org.gostroy.service.UserService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;

/**
 * Created by panser on 5/23/14.
 */
@Controller
@RequestMapping("/article")
public class ArticleController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired(required = true)
    private ArticleService articleService;
    @Autowired(required = true)
    private UserService userService;

    @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
    public String listArticle(Model model){
        log.trace("listArticle() start ...");
        model.addAttribute("articles", articleService.findAll());
        return "/article/articleList";
    }

    @RequestMapping(value = {"/edit/{id}"}, method = RequestMethod.GET)
    public String editArticle(Model model, @PathVariable String id){
        model.addAttribute("article", articleService.find(Long.parseLong(id)));
        return "/article/articleMod";
    }
    @RequestMapping(value = {"/edit/{id}"}, method = RequestMethod.POST)
    public String editArticle(Model model, @ModelAttribute("article") Article articleFromForm, BindingResult articleFromFormError,
                           @PathVariable(value = "id") String id
    ) throws IOException {
        String viewName;
        log.debug("editArticle(), articleFromForm.id = " + articleFromForm.getId());
        if(articleFromFormError.hasErrors()){
            model.addAttribute("articleFromFormError", articleFromFormError);
            viewName = "/article/articleMod";
        }
        else{
            articleService.save(articleFromForm);
            viewName = "redirect:/article/list";
        }
        return viewName;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newGET(Model model){
        model.addAttribute("article", new Article());
        return "/article/articleMod";
    }

//    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String newPOST(@Valid @ModelAttribute("article") Article articleFromForm, BindingResult result) throws MessagingException {
        String viewName;
        if(result.hasErrors()){
            viewName = "/article/articleMod";
        }
        else{
            log.trace("newPOST(), articleFromForm1: " + articleFromForm);
//            articleService.save(articleFromForm);

            String login = SecurityContextHolder.getContext().getAuthentication().getName();

            User author = userService.findByLogin(login);
            log.trace("newPOST(), author: " + author);
//            author.addArticle(articleFromForm);
//            userService.save(author);
//            User author = new User();
            articleFromForm.setAuthor(author);
            log.trace("newPOST(), articleFromForm2: " + articleFromForm);
            articleService.save(articleFromForm);

            viewName = "redirect:/article/list";
        }
        return viewName;
    }

}
