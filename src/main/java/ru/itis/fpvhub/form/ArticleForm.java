package ru.itis.fpvhub.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

public class ArticleForm {

    @NotBlank(message = "Название обязательно")
    @Size(min = 8, max = 180, message = "Название должно быть от 8 до 180 символов")
    private String title;

    @NotBlank(message = "Краткое описание обязательно")
    @Size(min = 30, max = 500, message = "Описание должно быть от 30 до 500 символов")
    private String summary;

    @NotBlank(message = "Текст статьи обязателен")
    @Size(min = 120, max = 20000, message = "Текст статьи должен быть от 120 до 20000 символов")
    private String content;

    @Size(max = 600, message = "Ссылка на обложку слишком длинная")
    private String coverImageUrl;

    @Size(max = 600, message = "Ссылка на видео слишком длинная")
    private String videoUrl;

    @NotNull(message = "Выберите категорию")
    private Long categoryId;

    private Set<Long> tagIds = new LinkedHashSet<>();

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
