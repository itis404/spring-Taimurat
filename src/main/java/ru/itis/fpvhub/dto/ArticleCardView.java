package ru.itis.fpvhub.dto;

import ru.itis.fpvhub.entity.enums.ArticleStatus;

import java.time.OffsetDateTime;
import java.util.List;

public class ArticleCardView {
    private final Long id;
    private final String title;
    private final String slug;
    private final String summary;
    private final String authorUsername;
    private final String authorDisplayName;
    private final String categoryName;
    private final String categorySlug;
    private final ArticleStatus status;
    private final String statusDisplayName;
    private final long viewsCount;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime publishedAt;
    private final List<TagOption> tags;

    public ArticleCardView(
            Long id,
            String title,
            String slug,
            String summary,
            String authorUsername,
            String authorDisplayName,
            String categoryName,
            String categorySlug,
            ArticleStatus status,
            String statusDisplayName,
            long viewsCount,
            OffsetDateTime createdAt,
            OffsetDateTime publishedAt,
            List<TagOption> tags
    ) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.summary = summary;
        this.authorUsername = authorUsername;
        this.authorDisplayName = authorDisplayName;
        this.categoryName = categoryName;
        this.categorySlug = categorySlug;
        this.status = status;
        this.statusDisplayName = statusDisplayName;
        this.viewsCount = viewsCount;
        this.createdAt = createdAt;
        this.publishedAt = publishedAt;
        this.tags = tags;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getSlug() { return slug; }
    public String getSummary() { return summary; }
    public String getAuthorUsername() { return authorUsername; }
    public String getAuthorDisplayName() { return authorDisplayName; }
    public String getCategoryName() { return categoryName; }
    public String getCategorySlug() { return categorySlug; }
    public ArticleStatus getStatus() { return status; }
    public String getStatusDisplayName() { return statusDisplayName; }
    public long getViewsCount() { return viewsCount; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getPublishedAt() { return publishedAt; }
    public List<TagOption> getTags() { return tags; }
}
