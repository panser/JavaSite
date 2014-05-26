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

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String listArticle(Model model){
        log.trace("listArticle() start ...");
        model.addAttribute("articles", articleService.findAll());
        return "/article/articleList";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String newGET(Model model){
        log.trace("newGET(), RequestMethod.GET");
        model.addAttribute("article", new Article());
        model.addAttribute("formMethod", "POST");
        return "/article/articleMod";
    }
//    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String newPOST(@Valid @ModelAttribute("article") Article articleFromForm, BindingResult result) throws MessagingException {
        log.trace("newGET(), RequestMethod.POST");
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

            viewName = "redirect:/article/";
        }
        return viewName;
    }

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
    public String editArticle(Model model, @PathVariable String id){
        model.addAttribute("article", articleService.find(Long.parseLong(id)));
        model.addAttribute("formMethod", "PUT");
        return "/article/articleMod";
    }
    @RequestMapping(value = {"/{id}"}, method = RequestMethod.PUT)
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
//            articleService.save(articleFromForm);
            Article article = articleService.find(Long.parseLong(id));
            article.setText(articleFromForm.getText());
            article.setTitle(articleFromForm.getTitle());
            articleService.save(article);
            viewName = "redirect:/article/";
        }
        return viewName;
    }

    @RequestMapping(value = {"/{id}/delete"}, method = RequestMethod.GET)
    public String deleteArticle(Model model, @PathVariable String id){
        Article deleteArticle = articleService.find(Long.parseLong(id));
        articleService.delete(deleteArticle);
        return "redirect:/article/";
    }
}
