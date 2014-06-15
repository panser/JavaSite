package ua.org.gostroy.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.org.gostroy.model.*;
import ua.org.gostroy.model.xmlwrapper.ArticleList;
import ua.org.gostroy.service.ArticleService;
import ua.org.gostroy.service.CommentService;
import ua.org.gostroy.service.UserService;
import ua.org.gostroy.service.VisitorService;

import javax.mail.MessagingException;
import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
    @Autowired(required = true)
    VisitorService visitorService;
    @Autowired(required = true)
    CommentService commentService;

    @RequestMapping(value = {"/", "list"}, method = RequestMethod.GET, produces = {MediaType.TEXT_HTML_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE})
    public String listArticle(Model model){
        log.trace("listArticle() start ...");
        model.addAttribute("articles", articleService.findAll());
        return "/article/articleList";
    }
    @RequestMapping(value = {"/", "/list"}, method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ArticleList listArticleXML(Model model){
        log.trace("listArticleXML() start ...");
        ArticleList articleList = new ArticleList();
        articleList.getArticleList().addAll(articleService.findAll());
        return articleList;
    }
/*
    @RequestMapping(value = {"/", "/list"}, method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE})
    public String listArticleXML(Model model){
        log.trace("listArticleXML() start ...");
        ArticleList articleList = new ArticleList();
        articleList.getArticleList().addAll(articleService.findAll());
        model.addAttribute("articleList", articleList);
        return "/article/articleList";
    }
*/
    @RequestMapping(value = {"/", "list"}, method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<Article> listArticleJSON(Model model){
        log.trace("listArticleJSON() start ...");
        return articleService.findAll();
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String newGET(Model model){
        log.trace("newGET(), RequestMethod.GET");
        model.addAttribute("article", new Article());
        return "/article/articleEdit";
    }
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String newPOST(@Valid @ModelAttribute("article") Article articleFromForm, BindingResult result, Principal user) throws MessagingException {
        log.trace("newGET(), RequestMethod.POST");
        String viewName;
        if(result.hasErrors()){
            viewName = "/article/articleEdit";
        }
        else{
            log.trace("newPOST(), articleFromForm1: " + articleFromForm);
//            articleService.save(articleFromForm);

            String login = (user == null) ? "guest" : user.getName();

            User author = userService.findByLogin(login);
            log.trace("newPOST(), author: " + author);
//            author.addArticle(articleFromForm);
//            userService.save(author);
//            User author = new User();
            articleFromForm.setAuthor(author);
            log.trace("newPOST(), articleFromForm2: " + articleFromForm);
            articleService.create(articleFromForm);

            viewName = "redirect:/article/";
        }
        return viewName;
    }

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
    public String readArticle(Model model, @PathVariable String id,
                          HttpEntity<byte[]> requestEntity, ServletRequest servletRequest){
        Article article = articleService.find(Long.parseLong(id));
        String userAgent = requestEntity.getHeaders().getFirst("USER-AGENT");
        String ipAddress = requestEntity.getHeaders().getFirst("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = servletRequest.getRemoteAddr();
        }

        Visitor visitor = new Visitor();
        visitor.setIp(ipAddress);
        visitor.setUserAgent(userAgent);
        visitor.setArticle(article);
        visitor.setCreateDate(new Date());
        visitorService.save(visitor);

        model.addAttribute("article", article);
        model.addAttribute("countUniqueVisitors", visitorService.findCountUniqueVisitor(article));
        model.addAttribute("countVisitors", visitorService.findByArticle(article).size());

        if(!model.containsAttribute("comment")){
            model.addAttribute("comment", new Comment());
        }

        List<Comment> comments = commentService.findByArticle(article);
        Collections.sort(comments, Comment.сommentDepthComparator);
        model.addAttribute("comments", comments);

        return "/article/articleRead";
    }

    @RequestMapping(value = {"/{id}/edit"}, method = RequestMethod.GET)
    public String editArticle(Model model, @PathVariable String id){
        model.addAttribute("article", articleService.find(Long.parseLong(id)));
        model.addAttribute("formMethod", "PUT");
        return "/article/articleEdit";
    }
    @RequestMapping(value = {"/{id}/edit"}, method = RequestMethod.PUT)
    public String editArticle(@Valid @ModelAttribute("article") Article articleFromForm, BindingResult articleFromFormError,
                              @PathVariable(value = "id") String id
    ) throws IOException {
        String viewName;
        log.debug("editArticle(), articleFromForm.id = " + articleFromForm.getId());
        if(articleFromFormError.hasErrors()){
            viewName = "/article/articleEdit";
        }
        else{
            Article article = articleService.find(Long.parseLong(id));
            article.setText(articleFromForm.getText());
            article.setDescription(articleFromForm.getDescription());
            article.setTitle(articleFromForm.getTitle());
            articleService.update(article);
            viewName = "redirect:/article/" + id;
        }
        return viewName;
    }

    @RequestMapping(value = {"/{id}/delete"}, method = RequestMethod.GET)
    public String deleteArticle(Model model, @PathVariable String id){
        Article deleteArticle = articleService.find(Long.parseLong(id));
        articleService.delete(deleteArticle);
        return "redirect:/article/";
    }

    @RequestMapping(value = {"/{id}/comment"}, method = RequestMethod.POST)
    public String addComment(Model model, @Valid @ModelAttribute("comment") Comment commentFromForm,
                             BindingResult commentFromFormError, RedirectAttributes redirectAttributes,
                             @PathVariable String id, @RequestParam("parentComment") String parentComment)
            throws IOException {
        Article article = articleService.find(Long.parseLong(id));
        log.trace("addComment(), parentComment: " + parentComment);
        if(commentFromFormError.hasErrors()){
            log.debug("addComment(), commentFromFormError: " + commentFromFormError.toString());
//            redirectAttributes.addFlashAttribute("flashMessageAdd", messageSource.getMessage("flashMessageAdd", null, LocaleContextHolder.getLocale()));
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.comment", commentFromFormError);
            redirectAttributes.addFlashAttribute("comment", commentFromForm);
            redirectAttributes.addFlashAttribute("parentComment", parentComment);
        }
        else{
            log.debug("addComment(), commentFromForm: " + commentFromForm);
            commentFromForm.setArticle(article);
            if(!parentComment.equals("0")) {
                Comment comment = commentService.find(Long.parseLong(parentComment));
                commentFromForm.setParent(comment);
                commentFromForm.setDepth(comment.getDepth() + 1);
            }
            commentService.create(commentFromForm);
        }
        return "redirect:/article/" + article.getId();
    }
}
