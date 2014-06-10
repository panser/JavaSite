package ua.org.gostroy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.org.gostroy.model.Article;
import ua.org.gostroy.model.Comment;

import java.util.List;

/**
 * Created by panser on 5/23/14.
 */
@Repository
public interface CommentRepository  extends JpaRepository<Comment, Long> {
    List<Comment> findByArticle(Article article);
}
