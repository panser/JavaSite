package ua.org.gostroy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.org.gostroy.entity.Article;
import ua.org.gostroy.repository.ArticleRepository;

import java.util.List;

/**
 * Created by panser on 5/23/14.
 */
@Service
@Transactional
public class ArticleService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ArticleRepository articleRepository;

    public Article find(Long id) {
        log.trace("find ...");
        Article article = articleRepository.findOne(id);
        log.trace("find.");
        return article;
    }

    public List<Article> findAll() {
        log.trace("findAll ...");
        List<Article>  articles = articleRepository.findAll();
        log.trace("findAll.");
        return articles;
    }

    public Long save(Article article) {
        log.trace("save ...");
        articleRepository.save(article);
        log.trace("save.");
        return article.getId();
    }

    public void update(Article article) {
        log.trace("update ...");
        articleRepository.save(article);
        log.trace("update.");
    }

    public void delete(Article article) {
        log.trace("delete ...");
        articleRepository.delete(article);
        log.trace("delete.");
    }

}
