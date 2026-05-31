package ru.itis.fpvhub.converter;

import org.springframework.stereotype.Component;
import ru.itis.fpvhub.dto.api.ArticleApiRequest;
import ru.itis.fpvhub.dto.api.ArticleApiResponse;
import ru.itis.fpvhub.entity.ArticleEntity;
import ru.itis.fpvhub.entity.ProfileEntity;
import ru.itis.fpvhub.entity.TagEntity;
import ru.itis.fpvhub.form.ArticleForm;

import java.util.LinkedHashSet;

@Component
public class ArticleApiConverter {

    public ArticleApiResponse toResponse(ArticleEntity article) {
        ArticleApiResponse response = new ArticleApiResponse();
        response.setId(article.getId());
        response.setTitle(article.getTitle());
        response.setSlug(article.getSlug());
        response.setSummary(article.getSummary());
        response.setContent(article.getContent());
        response.setCoverImageUrl(article.getCoverImageUrl());
        response.setVideoUrl(article.getVideoUrl());
        response.setStatus(article.getStatus());
        response.setStatusDisplayName(article.getStatus().getDisplayName());
        response.setViewsCount(article.getViewsCount());
        response.setCreatedAt(article.getCreatedAt());
        response.setUpdatedAt(article.getUpdatedAt());
        response.setPublishedAt(article.getPublishedAt());

        ProfileEntity profile = article.getAuthor().getProfile();
        String displayName = profile == null ? article.getAuthor().getUsername() : profile.getDisplayName();
        response.setAuthor(new ArticleApiResponse.Author(
                article.getAuthor().getId(),
                article.getAuthor().getUsername(),
                displayName
        ));
        response.setCategory(new ArticleApiResponse.Category(
                article.getCategory().getId(),
                article.getCategory().getName(),
                article.getCategory().getSlug()
        ));
        response.setTags(article.getTags()
                .stream()
                .map(this::toTagResponse)
                .toList());
        return response;
    }

    public ArticleForm toForm(ArticleApiRequest request) {
        ArticleForm form = new ArticleForm();
        form.setTitle(request.getTitle());
        form.setSummary(request.getSummary());
        form.setContent(request.getContent());
        form.setCoverImageUrl(request.getCoverImageUrl());
        form.setVideoUrl(request.getVideoUrl());
        form.setCategoryId(request.getCategoryId());
        form.setTagIds(request.getTagIds() == null ? new LinkedHashSet<>() : request.getTagIds());
        form.setPublicationAction(request.getPublicationAction());
        return form;
    }

    private ArticleApiResponse.Tag toTagResponse(TagEntity tag) {
        return new ArticleApiResponse.Tag(tag.getId(), tag.getName(), tag.getSlug());
    }
}
