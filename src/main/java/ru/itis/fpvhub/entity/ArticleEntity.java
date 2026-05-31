package ru.itis.fpvhub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import ru.itis.fpvhub.entity.enums.ArticleStatus;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "articles")
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 180)
    private String title;

    @Column(name = "slug", nullable = false, unique = true, length = 220)
    private String slug;

    @Column(name = "summary", nullable = false, length = 500)
    private String summary;

    @Column(name = "content", nullable = false, columnDefinition = "text")
    private String content;

    @Column(name = "cover_image_url", length = 600)
    private String coverImageUrl;

    @Column(name = "video_url", length = 600)
    private String videoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 40)
    private ArticleStatus status;

    @Column(name = "views_count", nullable = false)
    private long viewsCount;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "published_at")
    private OffsetDateTime publishedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "article_tags",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagEntity> tags = new LinkedHashSet<>();

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<CommentEntity> comments = new ArrayList<>();

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<ArticleReactionEntity> reactions = new ArrayList<>();

    protected ArticleEntity() {
    }

    public ArticleEntity(
            String title,
            String slug,
            String summary,
            String content,
            String coverImageUrl,
            String videoUrl,
            ArticleStatus status,
            UserEntity author,
            CategoryEntity category,
            Set<TagEntity> tags
    ) {
        this.title = title;
        this.slug = slug;
        this.summary = summary;
        this.content = content;
        this.coverImageUrl = coverImageUrl;
        this.videoUrl = videoUrl;
        this.status = status;
        this.author = author;
        this.category = category;
        this.tags = new LinkedHashSet<>(tags);
    }

    @PrePersist
    void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.status == ArticleStatus.PUBLISHED && this.publishedAt == null) {
            this.publishedAt = now;
        }
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public void submitForReview() {
        this.status = ArticleStatus.PENDING_REVIEW;
    }

    public void saveAsDraft() {
        this.status = ArticleStatus.DRAFT;
    }

    public void publish() {
        this.status = ArticleStatus.PUBLISHED;
        if (this.publishedAt == null) {
            this.publishedAt = OffsetDateTime.now();
        }
    }

    public void reject() {
        this.status = ArticleStatus.REJECTED;
    }

    public void archive() {
        this.status = ArticleStatus.ARCHIVED;
    }

    public void incrementViews() {
        this.viewsCount++;
    }

    public boolean isPublished() {
        return ArticleStatus.PUBLISHED.equals(this.status);
    }

    public Long getId() {
        return id;
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

    public long getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(long viewsCount) {
        this.viewsCount = viewsCount;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public OffsetDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(OffsetDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public Set<TagEntity> getTags() {
        return tags;
    }

    public void setTags(Set<TagEntity> tags) {
        this.tags = tags;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    public List<ArticleReactionEntity> getReactions() {
        return reactions;
    }

    public void setReactions(List<ArticleReactionEntity> reactions) {
        this.reactions = reactions;
    }
}
