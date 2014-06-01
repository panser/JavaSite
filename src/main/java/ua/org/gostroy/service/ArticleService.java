package ua.org.gostroy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.org.gostroy.domain.Article;
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
    @Autowired
    private MutableAclService mutableAclService;

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

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("#article.author.login == authentication.name or hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Long save(Article article) {
        log.trace("save ...");
        articleRepository.save(article);
        log.trace("save(), article.getId(): " + article.getId());
        ObjectIdentity oid = new ObjectIdentityImpl(Article.class, article.getId());
        MutableAcl acl = mutableAclService.createAcl(oid);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        acl.insertAce(0, BasePermission.ADMINISTRATION, new PrincipalSid(user.getUsername()), true);
        acl.insertAce(1, BasePermission.DELETE, new GrantedAuthoritySid("ROLE_ADMIN"), true);
//        acl.insertAce(2, BasePermission.READ, new GrantedAuthoritySid("ROLE_ADMIN"), true);
        acl.insertAce(2, BasePermission.READ, new GrantedAuthoritySid("ROLE_USER"), true);
        mutableAclService.updateAcl(acl);

        log.trace("save.");
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
