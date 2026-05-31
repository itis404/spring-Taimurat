package ru.itis.fpvhub.dto;

import java.util.List;

public class ArticleStatisticsView {
    private final List<ArticleCardView> mostViewed;
    private final List<ArticleCardView> discussed;

    public ArticleStatisticsView(List<ArticleCardView> mostViewed, List<ArticleCardView> discussed) {
        this.mostViewed = mostViewed;
        this.discussed = discussed;
    }

    public List<ArticleCardView> getMostViewed() {
        return mostViewed;
    }

    public List<ArticleCardView> getDiscussed() {
        return discussed;
    }
}
