package ua.org.gostroy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.org.gostroy.entity.Article;
import ua.org.gostroy.entity.Comment;
import ua.org.gostroy.entity.User;

import java.util.List;

/**
 * Created by panser on 5/23/14.
 */
@Repository
public interface CommentRepository  extends JpaRepository<Comment, Long> {
    List<Comment> findByArticle(Article article);
}
