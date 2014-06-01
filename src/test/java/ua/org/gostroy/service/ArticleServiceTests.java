package ua.org.gostroy.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import ua.org.gostroy.domain.Article;
import ua.org.gostroy.domain.User;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by panser on 5/23/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({
        @ContextConfiguration(locations = { "classpath:/etc/spring/applicationContext.xml", "classpath:/etc/spring/applicationContext.d/*" }),
//        @ContextConfiguration(locations = { "classpath:/etc/spring/servletContext.xml", "classpath:/etc/spring/servletContext.d/*" })
})
public class ArticleServiceTests {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;

    private Article testArticle;
    @Before
    public void setup(){
        testArticle = new Article();
        testArticle.setTitle("jUnitArticle");
        testArticle.setDescription("jUnitArticle");
        User testUser = new User();
        testUser.setLogin("jUnitUser");
        testUser.setEmail("jUnitUser@gostroy.org.ua");
        testUser.setEnabled(true);
        testUser.setPassword("jUnitUser");
        testUser.setRole("ROLE_USER");
//        testUser.setRole("ROLE_ADMIN");
        testArticle.setAuthor(testUser);
        articleService.create(testArticle);
        log.trace("setup(), user: " + testUser);
        log.trace("setup(), testArticle.getAuthor(): " + testArticle.getAuthor());

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(testUser.getLogin(), testUser.getPassword());
        Authentication authenticationResult = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationResult);
    }
    @After
    public void destroy(){
        articleService.delete(testArticle);
        User userDelete = userService.findByLogin("jUnitUser");
        userService.delete(userDelete);
    }

    @Test
    public void findOne(){
        Article newArticle = articleService.find(testArticle.getId());
        Assert.assertEquals(newArticle.getId(), testArticle.getId());
    }

    @Test
    public void update(){
        Article updateArticle = articleService.find(testArticle.getId());
        articleService.update(updateArticle);
        Assert.assertEquals(updateArticle.getId(), testArticle.getId());
    }
}
