package ua.org.gostroy.web.view.atom;

import com.sun.syndication.feed.atom.Content;
import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;
import org.springframework.web.servlet.view.feed.AbstractAtomFeedView;
import ua.org.gostroy.model.Article;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by panser on 6/12/2014.
 */
public class ArticlesAtomView extends AbstractAtomFeedView {
    @Override
    protected void buildFeedMetadata(Map<String, Object> model, Feed feed, HttpServletRequest request) {
        feed.setId("tag:gostroy.org.ua");
        feed.setTitle("Articles");
        //feed.setUpdated(date);
    }

    @Override
    protected List<Entry> buildFeedEntries(Map<String, Object> map,
                     HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        List<Article> articles = (List<Article>)map.get("articles");
        List<Entry> entries = new ArrayList<Entry>(articles.size());

        for(Article article : articles){
            Entry entry = new Entry();
            entry.setId(String.format("tag:gostroy.org.ua,%s", article.getId()));
            entry.setTitle(String.format("Article: %s", article.getTitle()));
            //entry.setUpdated(article.getDate().toDate());

            Content summary = new Content();
            summary.setValue(article.getDescription() + "\n\n" + article.getText());
            entry.setSummary(summary);

            entries.add(entry);
        }

        httpServletResponse.setContentType("application/atom+xml");
        return entries;
    }
}
