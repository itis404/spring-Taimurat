package ru.itis.fpvhub.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.fpvhub.entity.ArticleEntity;
import ru.itis.fpvhub.entity.enums.ArticleStatus;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

    boolean existsBySlug(String slug);

    long countByStatus(ArticleStatus status);

    @Query("""
            select distinct a from ArticleEntity a
            join fetch a.author author
            left join fetch author.profile
            join fetch a.category
            left join fetch a.tags
            where a.status = ru.itis.fpvhub.entity.enums.ArticleStatus.PUBLISHED
            order by a.publishedAt desc nulls last, a.createdAt desc
            """)
    List<ArticleEntity> findPublishedArticles();

    @Query("""
            select distinct a from ArticleEntity a
            join fetch a.author author
            left join fetch author.profile
            join fetch a.category
            left join fetch a.tags
            where a.status = ru.itis.fpvhub.entity.enums.ArticleStatus.PUBLISHED
              and a.category.slug = :categorySlug
            order by a.publishedAt desc nulls last, a.createdAt desc
            """)
    List<ArticleEntity> findPublishedArticlesByCategorySlug(@Param("categorySlug") String categorySlug);

    @Query("""
            select distinct a from ArticleEntity a
            join fetch a.author author
            left join fetch author.profile
            join fetch a.category
            left join fetch a.tags
            where a.status = ru.itis.fpvhub.entity.enums.ArticleStatus.PUBLISHED
              and lower(a.title) like lower(concat('%', :query, '%'))
            order by a.publishedAt desc nulls last, a.createdAt desc
            """)
    List<ArticleEntity> searchPublishedByTitle(@Param("query") String query);

    @Query("""
            select distinct a from ArticleEntity a
            join fetch a.author author
            left join fetch author.profile
            join fetch a.category
            left join fetch a.tags
            where a.slug = :slug
              and a.status = ru.itis.fpvhub.entity.enums.ArticleStatus.PUBLISHED
            """)
    Optional<ArticleEntity> findPublishedDetailedBySlug(@Param("slug") String slug);

    @Query("""
            select distinct a from ArticleEntity a
            join fetch a.author author
            left join fetch author.profile
            join fetch a.category
            left join fetch a.tags
            where a.id = :id
            """)
    Optional<ArticleEntity> findDetailedById(@Param("id") Long id);

    @Query("""
            select distinct a from ArticleEntity a
            join fetch a.author author
            left join fetch author.profile
            join fetch a.category
            left join fetch a.tags
            where author.id = :authorId
            order by a.updatedAt desc
            """)
    List<ArticleEntity> findDetailedByAuthorId(@Param("authorId") Long authorId);

    @Query("""
            select distinct a from ArticleEntity a
            join fetch a.author author
            left join fetch author.profile
            join fetch a.category
            left join fetch a.tags
            where author.username = :username
              and a.status = ru.itis.fpvhub.entity.enums.ArticleStatus.PUBLISHED
            order by a.publishedAt desc nulls last, a.createdAt desc
            """)
    List<ArticleEntity> findPublishedByAuthorUsername(@Param("username") String username);

    @Query("""
            select distinct a from ArticleEntity a
            join fetch a.author author
            left join fetch author.profile
            join fetch a.category
            left join fetch a.tags
            order by a.updatedAt desc
            """)
    List<ArticleEntity> findAllForAdmin();


    @Query("""
            select distinct a from ArticleEntity a
            join fetch a.author author
            left join fetch author.profile
            join fetch a.category
            left join fetch a.tags
            where a.status = ru.itis.fpvhub.entity.enums.ArticleStatus.PUBLISHED
            order by a.viewsCount desc, a.publishedAt desc nulls last, a.createdAt desc
            """)
    List<ArticleEntity> findMostViewedPublished(Pageable pageable);

    @Query("""
            select distinct a from ArticleEntity a
            join fetch a.author author
            left join fetch author.profile
            join fetch a.category
            left join fetch a.tags
            where a.status = ru.itis.fpvhub.entity.enums.ArticleStatus.PUBLISHED
              and a.id in (
                  select c.article.id
                  from CommentEntity c
                  where c.deleted = false
                  group by c.article.id
                  having count(c.id) >= :minComments
              )
            order by a.publishedAt desc nulls last, a.createdAt desc
            """)
    List<ArticleEntity> findDiscussedPublishedArticles(@Param("minComments") long minComments);


    List<ArticleEntity> findTop6ByStatusOrderByPublishedAtDesc(ArticleStatus status);
}
