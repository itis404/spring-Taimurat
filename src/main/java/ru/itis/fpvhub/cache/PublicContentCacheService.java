package ru.itis.fpvhub.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import ru.itis.fpvhub.dto.ArticleCardView;
import ru.itis.fpvhub.dto.ArticleStatisticsView;
import ru.itis.fpvhub.dto.CategoryOption;
import ru.itis.fpvhub.dto.cache.CachedArticleCard;
import ru.itis.fpvhub.dto.cache.CachedArticleStatistics;
import ru.itis.fpvhub.dto.cache.CachedCategoryOption;
import ru.itis.fpvhub.service.ArticleService;
import ru.itis.fpvhub.service.CategoryService;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class PublicContentCacheService {

    private static final Logger log = LoggerFactory.getLogger(PublicContentCacheService.class);
    private static final Duration DEFAULT_TTL = Duration.ofMinutes(10);
    private static final Duration CATEGORIES_TTL = Duration.ofHours(1);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final ArticleService articleService;
    private final CategoryService categoryService;

    public PublicContentCacheService(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper,
            ArticleService articleService,
            CategoryService categoryService
    ) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.articleService = articleService;
        this.categoryService = categoryService;
    }

    public List<ArticleCardView> findLatestPublishedArticles() {
        Optional<List<CachedArticleCard>> cached = read(
                CacheKeys.LATEST_ARTICLES,
                new TypeReference<>() {
                }
        );
        if (cached.isPresent()) {
            log.debug("Redis cache hit: {}", CacheKeys.LATEST_ARTICLES);
            return cached.get().stream().map(CachedArticleCard::toView).toList();
        }

        List<ArticleCardView> articles = articleService.findLatestPublishedArticles();
        write(
                CacheKeys.LATEST_ARTICLES,
                articles.stream().map(CachedArticleCard::from).toList(),
                DEFAULT_TTL
        );
        return articles;
    }

    public List<CategoryOption> findCategoryOptions() {
        Optional<List<CachedCategoryOption>> cached = read(
                CacheKeys.CATEGORY_OPTIONS,
                new TypeReference<>() {
                }
        );
        if (cached.isPresent()) {
            log.debug("Redis cache hit: {}", CacheKeys.CATEGORY_OPTIONS);
            return cached.get().stream().map(CachedCategoryOption::toView).toList();
        }

        List<CategoryOption> categories = categoryService.findAllOptions();
        write(
                CacheKeys.CATEGORY_OPTIONS,
                categories.stream().map(CachedCategoryOption::from).toList(),
                CATEGORIES_TTL
        );
        return categories;
    }

    public ArticleStatisticsView getArticleStatistics() {
        Optional<CachedArticleStatistics> cached = read(
                CacheKeys.ARTICLE_STATISTICS,
                new TypeReference<>() {
                }
        );
        if (cached.isPresent()) {
            log.debug("Redis cache hit: {}", CacheKeys.ARTICLE_STATISTICS);
            return cached.get().toView();
        }

        ArticleStatisticsView statistics = articleService.getArticleStatistics();
        write(CacheKeys.ARTICLE_STATISTICS, CachedArticleStatistics.from(statistics), DEFAULT_TTL);
        return statistics;
    }

    private <T> Optional<T> read(String key, TypeReference<T> typeReference) {
        try {
            String json = redisTemplate.opsForValue().get(key);
            if (json == null || json.isBlank()) {
                return Optional.empty();
            }
            return Optional.of(objectMapper.readValue(json, typeReference));
        } catch (Exception exception) {
            log.warn("Unable to read Redis cache key {}", key, exception);
            return Optional.empty();
        }
    }

    private void write(String key, Object value, Duration ttl) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, ttl);
            log.debug("Redis cache written: {} ttl={}", key, ttl);
        } catch (Exception exception) {
            log.warn("Unable to write Redis cache key {}", key, exception);
        }
    }
}
