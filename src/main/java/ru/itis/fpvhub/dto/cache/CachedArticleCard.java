package ru.itis.fpvhub.dto.cache;

import ru.itis.fpvhub.dto.ArticleCardView;
import ru.itis.fpvhub.entity.enums.ArticleStatus;

import java.time.OffsetDateTime;
import java.util.List;

public record CachedArticleCard(
        Long id,
        String title,
        String slug,
        String summary,
        String authorUsername,
        String authorDisplayName,
        String categoryName,
        String categorySlug,
        String status,
        String statusDisplayName,
        long viewsCount,
        OffsetDateTime createdAt,
        OffsetDateTime publishedAt,
        List<CachedTagOption> tags
) {

    public static CachedArticleCard from(ArticleCardView source) {
        return new CachedArticleCard(
                source.getId(),
                source.getTitle(),
                source.getSlug(),
                source.getSummary(),
                source.getAuthorUsername(),
                source.getAuthorDisplayName(),
                source.getCategoryName(),
                source.getCategorySlug(),
                source.getStatus() == null ? null : source.getStatus().name(),
                source.getStatusDisplayName(),
                source.getViewsCount(),
                source.getCreatedAt(),
                source.getPublishedAt(),
                source.getTags() == null
                        ? List.of()
                        : source.getTags().stream().map(CachedTagOption::from).toList()
        );
    }

    public ArticleCardView toView() {
        return new ArticleCardView(
                id,
                title,
                slug,
                summary,
                authorUsername,
                authorDisplayName,
                categoryName,
                categorySlug,
                status == null ? null : ArticleStatus.valueOf(status),
                statusDisplayName,
                viewsCount,
                createdAt,
                publishedAt,
                tags == null ? List.of() : tags.stream().map(CachedTagOption::toView).toList()
        );
    }
}
