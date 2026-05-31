package ru.itis.fpvhub.dto.api;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.itis.fpvhub.entity.enums.ArticleStatus;

import java.time.OffsetDateTime;
import java.util.List;

@Schema(description = "Article API response")
public class ArticleApiResponse {

    private Long id;
    private String title;
    private String slug;
    private String summary;
    private String content;
    private String coverImageUrl;
    private String videoUrl;
    private ArticleStatus status;
    private String statusDisplayName;
    private long viewsCount;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime publishedAt;
    private Author author;
    private Category category;
    private List<Tag> tags;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public ArticleStatus getStatus() {
        return status;
    }

    public void setStatus(ArticleStatus status) {
        this.status = status;
    }

    public String getStatusDisplayName() {
        return statusDisplayName;
    }

    public void setStatusDisplayName(String statusDisplayName) {
        this.statusDisplayName = statusDisplayName;
    }

    public long getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(long viewsCount) {
        this.viewsCount = viewsCount;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OffsetDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(OffsetDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public static class Author {
        private Long id;
        private String username;
        private String displayName;

        public Author(Long id, String username, String displayName) {
            this.id = id;
            this.username = username;
            this.displayName = displayName;
        }

        public Long getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public static class Category {
        private Long id;
        private String name;
        private String slug;

        public Category(Long id, String name, String slug) {
            this.id = id;
            this.name = name;
            this.slug = slug;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getSlug() {
            return slug;
        }
    }

    public static class Tag {
        private Long id;
        private String name;
        private String slug;

        public Tag(Long id, String name, String slug) {
            this.id = id;
            this.name = name;
            this.slug = slug;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getSlug() {
            return slug;
        }
    }
}
