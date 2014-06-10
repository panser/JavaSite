package ua.org.gostroy.model;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;
import ua.org.gostroy.model.Article;
import ua.org.gostroy.model.Comment;
import ua.org.gostroy.model.User;

/**
 * Created by panser on 6/6/2014.
 */
public class ArticleTests {

    @Test
    @Transactional
    public void testHasComment(){
        Article article = new Article();
        User user = new User();
        user.setLogin("JUnitUser");
        Assert.assertNull(article.getAuthor());
        article.setAuthor(user);
        Assert.assertEquals(user,article.getAuthor());
    }
}
