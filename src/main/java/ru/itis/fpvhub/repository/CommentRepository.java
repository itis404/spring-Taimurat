package ru.itis.fpvhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.fpvhub.entity.CommentEntity;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query("""
            select c from CommentEntity c
            join fetch c.author author
            left join fetch author.profile
            join fetch c.article article
            join fetch article.author articleAuthor
            where article.id = :articleId
            order by c.createdAt asc
            """)
    List<CommentEntity> findDetailedByArticleId(@Param("articleId") Long articleId);

    @Query("""
            select c from CommentEntity c
            join fetch c.author author
            left join fetch author.profile
            join fetch c.article article
            join fetch article.author articleAuthor
            where c.id = :id
            """)
    Optional<CommentEntity> findDetailedById(@Param("id") Long id);

    long countByArticleIdAndDeletedFalse(Long articleId);

    long countByDeletedFalse();
}
