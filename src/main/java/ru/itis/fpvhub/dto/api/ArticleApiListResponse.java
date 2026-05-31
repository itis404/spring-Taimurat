package ru.itis.fpvhub.dto.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "List wrapper for article responses")
public class ArticleApiListResponse {

    private final int count;
    private final List<ArticleApiResponse> items;

    public ArticleApiListResponse(List<ArticleApiResponse> items) {
        this.items = items;
        this.count = items.size();
    }

    public int getCount() {
        return count;
    }

    public List<ArticleApiResponse> getItems() {
        return items;
    }
}
