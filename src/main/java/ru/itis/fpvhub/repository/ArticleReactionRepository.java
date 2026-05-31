package ru.itis.fpvhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.fpvhub.entity.ArticleReactionEntity;
import ru.itis.fpvhub.entity.enums.ArticleReactionType;

import java.util.Optional;

public interface ArticleReactionRepository extends JpaRepository<ArticleReactionEntity, Long> {

    Optional<ArticleReactionEntity> findByArticleIdAndUserId(Long articleId, Long userId);

    long countByArticleIdAndType(Long articleId, ArticleReactionType type);

    @Query("""
            select r from ArticleReactionEntity r
            join fetch r.article article
            join fetch r.user user
            where article.id = :articleId and user.id = :userId
            """)
    Optional<ArticleReactionEntity> findDetailedByArticleIdAndUserId(@Param("articleId") Long articleId, @Param("userId") Long userId);
}
