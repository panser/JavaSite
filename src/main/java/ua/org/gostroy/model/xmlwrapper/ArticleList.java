package ua.org.gostroy.model.xmlwrapper;

import ua.org.gostroy.model.Article;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by panser on 6/12/2014.
 */
@XmlRootElement
public class ArticleList {
    private List<Article> articleList;

    @XmlElement
    public List<Article> getArticleList() {
        if(articleList == null){
            articleList = new ArrayList<Article>();
        }
        return articleList;
    }
}
