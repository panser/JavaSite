package ua.org.gostroy.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.org.gostroy.domain.Article;
import ua.org.gostroy.domain.User;

/**
 * Created by panser on 5/23/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/etc/spring/applicationContext.xml", "classpath:/etc/spring/applicationContext.d/*"})
public class ArticleServiceTests {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserService userService;

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
        testUser.setRole("ROLE_ADMIN");
        testArticle.setAuthor(testUser);
        articleService.save(testArticle);
        log.trace("setup(), user: " + testUser);
        log.trace("setup(), testArticle.getAuthor(): " + testArticle.getAuthor());
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
        articleService.save(updateArticle);
        Assert.assertEquals(updateArticle.getId(), testArticle.getId());
    }
}
