package ua.org.gostroy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.org.gostroy.entity.Article;
import ua.org.gostroy.entity.Visitor;
import ua.org.gostroy.repository.VisitorRepository;

import java.util.List;

/**
 * Created by panser on 5/27/2014.
 */
@Service
@Transactional
public class VisitorService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private VisitorRepository visitorRepository;

    @Transactional(readOnly = true)
    public Visitor find(Long id) {
        log.trace("find ...");
        Visitor visitor = visitorRepository.findOne(id);
        log.trace("find.");
        return visitor;
    }

    @Transactional(readOnly = true)
    public List<Visitor> findByIp(String ip) {
        log.trace("findByIp ...");
        List<Visitor> visitors = visitorRepository.findByIp(ip);
        log.trace("findByIp.");
        return visitors;
    }

    @Transactional(readOnly = true)
    public List<Visitor> findDistinctByIp(String ip) {
        log.trace("findDistinctByIp ...");
        List<Visitor> visitors = visitorRepository.findDistinctByIp(ip);
        log.trace("findDistinctByIp.");
        return visitors;
    }

    @Transactional(readOnly = true)
    public Long findCountUniqueVisitor(Article article) {
        log.trace("findCountUniqueVisitor ...");
        Long count = visitorRepository.findCountUniqueVisitor(article);
        log.trace("findCountUniqueVisitor.");
        return count;
    }

    @Transactional(readOnly = true)
    public List<Visitor> findByArticle(Article article) {
        log.trace("findByArticle ...");
        List<Visitor> visitors = visitorRepository.findByArticle(article);
        log.trace("findByArticle.");
        return visitors;
    }

    @Transactional(readOnly = true)
    public List<Visitor> findAll() {
        log.trace("findAll ...");
        List<Visitor>  visitors = visitorRepository.findAll();
        log.trace("findAll.");
        return visitors;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long save(Visitor visitor) {
        log.trace("save ...");
        visitorRepository.save(visitor);
        log.trace("save.");
        return visitor.getId();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = Exception.class)
    public void delete(Visitor visitor) {
        log.trace("delete ...");
        visitorRepository.delete(visitor);
        log.trace("delete.");
    }

}
