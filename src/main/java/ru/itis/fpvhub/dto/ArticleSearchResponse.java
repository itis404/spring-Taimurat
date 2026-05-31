package ru.itis.fpvhub.dto;

import java.util.List;

public class ArticleSearchResponse {
    private final List<ArticleCardView> articles;
    private final int total;

    public ArticleSearchResponse(List<ArticleCardView> articles) {
        this.articles = articles;
        this.total = articles.size();
    }

    public List<ArticleCardView> getArticles() {
        return articles;
    }

    public int getTotal() {
        return total;
    }
}
