package ru.itis.fpvhub.dto;

public class ArticleDetailsView extends ArticleCardView {
    private final String content;
    private final String coverImageUrl;
    private final String videoUrl;
    private final boolean canEdit;

    public ArticleDetailsView(ArticleCardView card, String content, String coverImageUrl, String videoUrl, boolean canEdit) {
        super(
                card.getId(),
                card.getTitle(),
                card.getSlug(),
                card.getSummary(),
                card.getAuthorUsername(),
                card.getAuthorDisplayName(),
                card.getCategoryName(),
                card.getCategorySlug(),
                card.getStatus(),
                card.getStatusDisplayName(),
                card.getViewsCount(),
                card.getCreatedAt(),
                card.getPublishedAt(),
                card.getTags()
        );
        this.content = content;
        this.coverImageUrl = coverImageUrl;
        this.videoUrl = videoUrl;
        this.canEdit = canEdit;
    }

    public String getContent() {
        return content;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public boolean isCanEdit() {
        return canEdit;
    }
}
