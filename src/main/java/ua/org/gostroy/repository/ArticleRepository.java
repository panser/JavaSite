package ua.org.gostroy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.org.gostroy.model.Article;

/**
 * Created by panser on 5/23/14.
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
}
