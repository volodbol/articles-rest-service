package com.volod.articles.repository;

import com.volod.articles.model.Article;
import com.volod.articles.model.ArticleApplicationUser;
import com.volod.articles.model.ArticleSummary;
import com.volod.articles.model.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    Page<ArticleApplicationUser> findAllByColor(Color color, Pageable pageable);

    @Query("SELECT COUNT(a.id), a.applicationUser.username AS name FROM Article AS a " +
            "GROUP BY a.applicationUser HAVING COUNT(a.id) > 3")
    Page<ArticleSummary> findAllByArticlesMoreThanThree(Pageable pageable);

    Optional<Article> getOneById(Integer id);

}
