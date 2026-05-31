package ru.itis.fpvhub.dto.cache;

import ru.itis.fpvhub.dto.ArticleStatisticsView;

import java.util.List;

public record CachedArticleStatistics(
        List<CachedArticleCard> mostViewed,
        List<CachedArticleCard> discussed
) {

    public static CachedArticleStatistics from(ArticleStatisticsView source) {
        return new CachedArticleStatistics(
                source.getMostViewed().stream().map(CachedArticleCard::from).toList(),
                source.getDiscussed().stream().map(CachedArticleCard::from).toList()
        );
    }

    public ArticleStatisticsView toView() {
        return new ArticleStatisticsView(
                mostViewed == null ? List.of() : mostViewed.stream().map(CachedArticleCard::toView).toList(),
                discussed == null ? List.of() : discussed.stream().map(CachedArticleCard::toView).toList()
        );
    }
}
