package ua.org.gostroy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.org.gostroy.domain.Article;
import ua.org.gostroy.domain.Comment;
import ua.org.gostroy.domain.User;
import ua.org.gostroy.domain.Visitor;
import ua.org.gostroy.service.ArticleService;
import ua.org.gostroy.service.CommentService;
import ua.org.gostroy.service.UserService;
import ua.org.gostroy.service.VisitorService;

import javax.mail.MessagingException;
import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

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

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String listArticle(Model model){
        log.trace("listArticle() start ...");
        model.addAttribute("articles", articleService.findAll());
        return "/article/articleList";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String newGET(Model model){
        log.trace("newGET(), RequestMethod.GET");
        model.addAttribute("article", new Article());
        model.addAttribute("formMethod", "POST");
        return "/article/articleEdit";
    }
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String newPOST(@Valid @ModelAttribute("article") Article articleFromForm, BindingResult result) throws MessagingException {
        log.trace("newGET(), RequestMethod.POST");
        String viewName;
        if(result.hasErrors()){
            viewName = "/article/articleEdit";
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
//        Collections.sort(comments, Comment.CommentDepthComparator);
//        List<Comment> comments2= sortWithParent(comments);
        model.addAttribute("comments", comments);

        return "/article/articleRead";
    }

    @RequestMapping(value = {"/{id}/edit"}, method = RequestMethod.GET)
    public String editArticle(Model model, @PathVariable String id){
        model.addAttribute("article", articleService.find(Long.parseLong(id)));
        model.addAttribute("formMethod", "PUT");
        return "/article/articleEdit";
    }
    @PreAuthorize("#article.author.login == authentication.name or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = {"/{id}/edit"}, method = RequestMethod.PUT)
    public String editArticle(Model model, @ModelAttribute("article") Article articleFromForm, BindingResult articleFromFormError,
                              @PathVariable(value = "id") String id
    ) throws IOException {
        String viewName;
        log.debug("editArticle(), articleFromForm.id = " + articleFromForm.getId());
        if(articleFromFormError.hasErrors()){
            model.addAttribute("articleFromFormError", articleFromFormError);
            viewName = "/article/articleEdit";
        }
        else{
            Article article = articleService.find(Long.parseLong(id));
            article.setText(articleFromForm.getText());
            article.setDescription(articleFromForm.getDescription());
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
            if(parentComment != null) {
                Comment comment = commentService.find(Long.parseLong(parentComment));
                commentFromForm.setParent(comment);
                commentFromForm.setDepth(comment.getDepth() + 1);
            }
            commentService.save(commentFromForm);
        }
        return "redirect:/article/" + article.getId();
    }

/*
    List<Comment> sortWithParent(List<Comment> comments) {
        ArrayList<Comment> comments2 = new ArrayList<Comment>();
        if (!comments.isEmpty()) {
            comments2.add(comments.get(0));
            comments.remove(0);

            do {
                Long parentId = comments2.get(comments2.size() - 1).getId();
                for (Comment comment : comments) {
                    if (comment.getParent().getId() == parentId) {
                        comments2.add(comment);
                        comments.remove(comment);
                    }
                }
            } while (comments.size() != 0);

            return comments2;
        } else {
            return null;
        }
    }
*/
}
