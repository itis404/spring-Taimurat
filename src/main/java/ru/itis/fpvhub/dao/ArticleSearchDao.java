package ru.itis.fpvhub.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import ru.itis.fpvhub.entity.ArticleEntity;
import ru.itis.fpvhub.entity.CategoryEntity;
import ru.itis.fpvhub.entity.TagEntity;
import ru.itis.fpvhub.entity.UserEntity;
import ru.itis.fpvhub.entity.enums.ArticleStatus;
import ru.itis.fpvhub.form.ArticleSearchForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Repository
public class ArticleSearchDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<ArticleEntity> searchPublished(ArticleSearchForm form) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ArticleEntity> cq = cb.createQuery(ArticleEntity.class);
        Root<ArticleEntity> article = cq.from(ArticleEntity.class);

        Fetch<ArticleEntity, UserEntity> authorFetch = article.fetch("author", JoinType.INNER);
        authorFetch.fetch("profile", JoinType.LEFT);
        article.fetch("category", JoinType.INNER);
        article.fetch("tags", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(article.get("status"), ArticleStatus.PUBLISHED));

        if (hasText(form.getQ())) {
            String query = likeValue(form.getQ());
            predicates.add(cb.or(
                    cb.like(cb.lower(article.get("title")), query),
                    cb.like(cb.lower(article.get("summary")), query),
                    cb.like(cb.lower(article.get("content")), query)
            ));
        }

        if (hasText(form.getCategory())) {
            Join<ArticleEntity, CategoryEntity> category = article.join("category", JoinType.INNER);
            predicates.add(cb.equal(category.get("slug"), form.getCategory().trim()));
        }

        if (hasText(form.getTag())) {
            Join<ArticleEntity, TagEntity> tag = article.join("tags", JoinType.INNER);
            predicates.add(cb.equal(tag.get("slug"), form.getTag().trim()));
        }

        if (form.isVideoOnly()) {
            var videoUrl = article.get("videoUrl").as(String.class);
            predicates.add(cb.and(
                    cb.isNotNull(videoUrl),
                    cb.notEqual(cb.trim(videoUrl), "")
            ));
        }

        cq.select(article)
                .distinct(true)
                .where(predicates.toArray(Predicate[]::new))
                .orderBy(resolveOrder(cb, article, form.getSort()));

        TypedQuery<ArticleEntity> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    private List<Order> resolveOrder(CriteriaBuilder cb, Root<ArticleEntity> article, String sort) {
        String normalizedSort = sort == null ? "newest" : sort.trim().toLowerCase(Locale.ROOT);
        return switch (normalizedSort) {
            case "views" -> List.of(cb.desc(article.get("viewsCount")), cb.desc(article.get("publishedAt")), cb.desc(article.get("createdAt")));
            case "title" -> List.of(cb.asc(article.get("title")), cb.desc(article.get("publishedAt")));
            default -> List.of(cb.desc(article.get("publishedAt")), cb.desc(article.get("createdAt")));
        };
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private String likeValue(String value) {
        return "%" + value.trim().toLowerCase(Locale.ROOT) + "%";
    }
}
