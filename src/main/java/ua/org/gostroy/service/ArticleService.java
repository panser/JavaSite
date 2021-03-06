package ua.org.gostroy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.org.gostroy.model.Article;
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

    @Transactional(readOnly = true)
    public Article find(Long id) {
        log.trace("find ...");
        Article article = articleRepository.findOne(id);
        log.trace("find.");
        return article;
    }

    @Transactional(readOnly = true)
    public List<Article> findAll() {
        log.trace("findAll ...");
        List<Article>  articles = articleRepository.findAll();
        log.trace("findAll.");
        return articles;
    }

    @Transactional(readOnly = true)
    public Page<Article> findAllByPage(Pageable pageable) {
        log.trace("findAllByPage ...");
        Page<Article>  articles = articleRepository.findAll(pageable);
        log.trace("findAllByPage.");
        return articles;
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional(rollbackFor = Exception.class)
    public Long create(Article article) {
        log.trace("create ...");
        articleRepository.save(article);
        log.trace("create.");
        return article.getId();
    }

    @PreAuthorize("#article.author.login == authentication.name or hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = Exception.class)
    public Long update(Article article) {
        log.trace("update ...");
        articleRepository.save(article);
        log.trace("update.");
        return article.getId();
    }


    @PreAuthorize("#article.author.login == authentication.name or hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = Exception.class)
    public void delete(Article article) {
        log.trace("delete ...");
        articleRepository.delete(article);
        log.trace("delete.");
    }

}
