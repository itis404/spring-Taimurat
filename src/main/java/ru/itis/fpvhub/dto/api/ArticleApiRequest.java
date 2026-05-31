package ru.itis.fpvhub.dto.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Schema(description = "Request body for creating or updating an article")
public class ArticleApiRequest {

    @Schema(description = "Article title", example = "Как настроить Betaflight после первой сборки")
    @NotBlank(message = "Title is required")
    @Size(min = 8, max = 180, message = "Title must be between 8 and 180 characters")
    private String title;

    @Schema(description = "Short article summary", example = "Краткий чеклист базовой настройки Betaflight для первого FPV-квадрокоптера")
    @NotBlank(message = "Summary is required")
    @Size(min = 30, max = 500, message = "Summary must be between 30 and 500 characters")
    private String summary;

    @Schema(description = "Full article content", example = "Первым делом нужно проверить ориентацию полетного контроллера, протокол приемника, порядок моторов и failsafe...")
    @NotBlank(message = "Content is required")
    @Size(min = 120, max = 20000, message = "Content must be between 120 and 20000 characters")
    private String content;

    @Schema(description = "Optional cover image URL", example = "https://example.com/fpv-cover.jpg")
    @Size(max = 600, message = "Cover image URL is too long")
    private String coverImageUrl;

    @Schema(description = "Optional video URL", example = "https://www.youtube.com/watch?v=dQw4w9WgXcQ")
    @Size(max = 600, message = "Video URL is too long")
    private String videoUrl;

    @Schema(description = "Category id", example = "1")
    @NotNull(message = "Category id is required")
    private Long categoryId;

    @Schema(description = "Tag ids", example = "[1, 2]")
    private Set<Long> tagIds = new LinkedHashSet<>();

    @Schema(description = "Publication action. Use draft to save as draft, any other value sends article to moderation", example = "review")
    private String publicationAction = "review";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Set<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<Long> tagIds) {
        this.tagIds = tagIds == null ? new LinkedHashSet<>() : tagIds;
    }

    public String getPublicationAction() {
        return publicationAction;
    }

    public void setPublicationAction(String publicationAction) {
        this.publicationAction = publicationAction;
    }
}
