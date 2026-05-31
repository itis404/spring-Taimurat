package ru.itis.fpvhub.converter;

import org.springframework.stereotype.Component;
import ru.itis.fpvhub.dto.ArticleCardView;
import ru.itis.fpvhub.dto.ArticleDetailsView;
import ru.itis.fpvhub.dto.CategoryOption;
import ru.itis.fpvhub.dto.TagOption;
import ru.itis.fpvhub.entity.ArticleEntity;
import ru.itis.fpvhub.entity.CategoryEntity;
import ru.itis.fpvhub.entity.TagEntity;
import ru.itis.fpvhub.form.ArticleForm;

import java.util.LinkedHashSet;
import java.util.List;

@Component
public class ArticleConverter {

    public ArticleCardView toCard(ArticleEntity article) {
        String displayName = article.getAuthor().getProfile() == null
                ? article.getAuthor().getUsername()
                : article.getAuthor().getProfile().getDisplayName();

        List<TagOption> tags = article.getTags()
                .stream()
                .map(this::toTagOption)
                .toList();

        return new ArticleCardView(
                article.getId(),
                article.getTitle(),
                article.getSlug(),
                article.getSummary(),
                article.getAuthor().getUsername(),
                displayName,
                article.getCategory().getName(),
                article.getCategory().getSlug(),
                article.getStatus(),
                article.getStatus().getDisplayName(),
                article.getViewsCount(),
                article.getCreatedAt(),
                article.getPublishedAt(),
                tags
        );
    }

    public ArticleDetailsView toDetails(ArticleEntity article, boolean canEdit) {
        return new ArticleDetailsView(
                toCard(article),
                article.getContent(),
                article.getCoverImageUrl(),
                article.getVideoUrl(),
                canEdit
        );
    }

    public ArticleForm toForm(ArticleEntity article) {
        ArticleForm form = new ArticleForm();
        form.setTitle(article.getTitle());
        form.setSummary(article.getSummary());
        form.setContent(article.getContent());
        form.setCoverImageUrl(article.getCoverImageUrl());
        form.setVideoUrl(article.getVideoUrl());
        form.setCategoryId(article.getCategory().getId());
        form.setTagIds(article.getTags()
                .stream()
                .map(TagEntity::getId)
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new)));
        return form;
    }

    public void updateEntity(ArticleEntity article, ArticleForm form, CategoryEntity category, java.util.Set<TagEntity> tags) {
        article.setTitle(clean(form.getTitle()));
        article.setSummary(clean(form.getSummary()));
        article.setContent(clean(form.getContent()));
        article.setCoverImageUrl(emptyToNull(form.getCoverImageUrl()));
        article.setVideoUrl(emptyToNull(form.getVideoUrl()));
        article.setCategory(category);
        article.setTags(new LinkedHashSet<>(tags));
    }

    public CategoryOption toCategoryOption(CategoryEntity category) {
        return new CategoryOption(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription()
        );
    }

    public TagOption toTagOption(TagEntity tag) {
        return new TagOption(tag.getId(), tag.getName(), tag.getSlug());
    }

    private String clean(String value) {
        return value == null ? null : value.trim();
    }

    private String emptyToNull(String value) {
        if (value == null || value.trim().isBlank()) {
            return null;
        }
        return value.trim();
    }
}
