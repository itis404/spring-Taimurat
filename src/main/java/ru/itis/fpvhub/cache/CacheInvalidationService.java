package ru.itis.fpvhub.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CacheInvalidationService {

    private static final Logger log = LoggerFactory.getLogger(CacheInvalidationService.class);

    private final StringRedisTemplate redisTemplate;

    public CacheInvalidationService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void evictPublicContentCaches() {
        try {
            redisTemplate.delete(List.of(
                    CacheKeys.LATEST_ARTICLES,
                    CacheKeys.CATEGORY_OPTIONS,
                    CacheKeys.ARTICLE_STATISTICS
            ));
            log.debug("Evicted public content Redis caches");
        } catch (RuntimeException exception) {
            log.warn("Unable to evict public content Redis caches", exception);
        }
    }
}
