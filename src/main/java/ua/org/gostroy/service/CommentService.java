package ua.org.gostroy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.org.gostroy.model.Article;
import ua.org.gostroy.model.Comment;
import ua.org.gostroy.repository.CommentRepository;

import java.util.List;

/**
 * Created by panser on 5/27/2014.
 */
@Service
@Transactional
public class CommentService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public Comment find(Long id) {
        log.trace("find ...");
        Comment comment = commentRepository.findOne(id);
        log.trace("find.");
        return comment;
    }

    @Transactional(readOnly = true)
    public List<Comment> findByArticle(Article article) {
        log.trace("findByArticle ...");
        List<Comment> comments = commentRepository.findByArticle(article);
        log.trace("findByArticle.");
        return comments;
    }


    @Transactional(readOnly = true)
    public List<Comment> findAll() {
        log.trace("findAll ...");
        List<Comment>  comments = commentRepository.findAll();
        log.trace("findAll.");
        return comments;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long create(Comment comment) {
        log.trace("create ...");
        commentRepository.save(comment);
        log.trace("create.");
        return comment.getId();
    }

    @PreAuthorize("#comment.author.login == authentication.name or hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = Exception.class)
    public Long update(Comment comment) {
        log.trace("update ...");
        commentRepository.save(comment);
        log.trace("update.");
        return comment.getId();
    }

    @PreAuthorize("#comment.author.login == authentication.name or hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = Exception.class)
    public void delete(Comment comment) {
        log.trace("delete ...");
        commentRepository.delete(comment);
        log.trace("delete.");
    }
}
