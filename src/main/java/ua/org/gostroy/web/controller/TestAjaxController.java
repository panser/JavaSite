package ua.org.gostroy.web.controller;

import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.rss.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.org.gostroy.model.JavaBean;
import ua.org.gostroy.model.User;
import ua.org.gostroy.web.util.AjaxUtils;

import javax.validation.Valid;

/**
 * Created by panser on 6/13/2014.
 */
@Controller
@RequestMapping("/test/ajax")
public class TestAjaxController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    // StringHttpMessageConverter
    @RequestMapping(value="/sendPlainText", method=RequestMethod.POST)
    public @ResponseBody String withBody(@RequestBody String body) {
        return "Posted request body '" + body + "'";
    }
    @RequestMapping("getPlainText")
    public @ResponseBody
    String simple() {        return "Hello world!";    }


    // Form encoded data (application/x-www-form-urlencoded)
    @RequestMapping(value="/sendFormUrlencoded", method=RequestMethod.POST)
    public @ResponseBody String readForm(@ModelAttribute JavaBean bean) {
        return "Read x-www-form-urlencoded: " + bean;
    }
    @RequestMapping(value="/getFormUrlencoded", method=RequestMethod.GET)
    public @ResponseBody MultiValueMap<String, Object> writeForm() {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("foo", "bar");
        map.add("fruit", "apple");
        return map;
    }

    // MappingJacksonHttpMessageConverter (requires Jackson on the classpath - particularly useful for serving JavaScript clients that expect to work with JSON)
    @RequestMapping(value="/sendJson", method= RequestMethod.POST, consumes= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String byConsumes(@RequestBody JavaBean javaBean) {
        log.debug("byConsumes(), javaBean: " + javaBean);
        return "Mapped by path + method + consumable media type (javaBean '" + javaBean + "')";
    }
    @RequestMapping(value="/getJson", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody JavaBean byProducesJson() {
        return new JavaBean();
    }


    // Jaxb2RootElementHttpMessageConverter (requires JAXB2 on the classpath - useful for serving clients that expect to work with XML)
    @RequestMapping(value="/sendXml", method=RequestMethod.POST)
    public @ResponseBody String readXml(@RequestBody JavaBean bean) {
        return "Read from XML: " + bean;
    }
    @RequestMapping(value="/getXml", method=RequestMethod.GET, produces=MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody JavaBean byProducesXml() {
        return new JavaBean();
    }


    // AtomFeedHttpMessageConverter (requires Rome on the classpath - useful for serving Atom feeds)
    @RequestMapping(value="/sendAtom", method=RequestMethod.POST)
    public @ResponseBody String readFeed(@RequestBody Feed feed) {
        return "Read " + feed.getTitle();
    }
    @RequestMapping(value="/getAtom", method=RequestMethod.GET)
    public @ResponseBody Feed writeFeed() {
        Feed feed = new Feed();
        feed.setFeedType("atom_1.0");
        feed.setTitle("My Atom feed");
        return feed;
    }

    // RssChannelHttpMessageConverter (requires Rome on the classpath - useful for serving RSS feeds)
    @RequestMapping(value="/sendRss", method=RequestMethod.POST)
    public @ResponseBody String readChannel(@RequestBody Channel channel) {
        return "Read " + channel.getTitle();
    }
    @RequestMapping(value="/getRss", method=RequestMethod.GET)
    public @ResponseBody Channel writeChannel() {
        Channel channel = new Channel();
        channel.setFeedType("rss_2.0");
        channel.setTitle("My RSS feed");
        channel.setDescription("Description");
        channel.setLink("http://localhost:8082/JavaSite/rss");
        return channel;
    }

    @ModelAttribute
    public void ajaxAttribute(WebRequest request, Model model) {
        model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
    }
    @RequestMapping(value = "/sendPostForm", method=RequestMethod.POST)
    public String sendPostFormPOST(@Valid User user, BindingResult result,
                                   @ModelAttribute("ajaxRequest") boolean ajaxRequest,
                                   Model model, RedirectAttributes redirectAttrs) {
        if(result.hasErrors()){
            log.trace("sendPostFormPOST(), receive result.hasErrors()");
            return "/test/ajax";
        }
        log.trace("sendPostFormPOST(), receive user: " + user);
        if(ajaxRequest){
            log.trace("sendPostFormPOST(), receive ajaxRequest");
            model.addAttribute("message", "receive user: " + user);
            return "/test/ajax";
        }
        else{
            log.trace("sendPostFormPOST(), receive normal request");
            redirectAttrs.addFlashAttribute("message", "receive user: " + user);
            return "redirect:/test/ajax";
        }
    }

}
