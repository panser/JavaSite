package ua.org.gostroy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.org.gostroy.domain.Article;
import ua.org.gostroy.domain.Visitor;

import java.util.List;

/**
 * Created by panser on 5/27/2014.
 */
@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    List<Visitor> findByIp(String ip);
    List<Visitor> findDistinctByIp(String ip);
    List<Visitor> findByArticle(Article article);

/*
//    @Query("select b.ip from Visitor b where b.article = ?1")
    @Query("select b.ip from Visitor b where b.article = :article")
    List<String> findDistinctIp(@Param("article") Article article);
*/

    @Query(value = "SELECT COUNT(DISTINCT ip) FROM visitors WHERE article_id = ?1", nativeQuery = true)
    Long findCountUniqueVisitor(Article article);
}
