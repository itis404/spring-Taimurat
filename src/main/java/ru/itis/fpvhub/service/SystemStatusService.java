package ru.itis.fpvhub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.itis.fpvhub.dto.SystemStatusView;

import java.time.Duration;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemStatusService {

    private static final String REDIS_PROBE_KEY = "fpvhub:system:probe";
    private static final String REDIS_PROBE_VALUE = "redis-ok";

    private final JdbcTemplate jdbcTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final Environment environment;

    public SystemStatusView getStatus() {
        String databaseVersion = "unavailable";
        boolean databaseAvailable = false;

        try {
            databaseVersion = jdbcTemplate.queryForObject("select version()", String.class);
            databaseAvailable = databaseVersion != null && !databaseVersion.isBlank();
        } catch (Exception exception) {
            log.warn("Database status check failed", exception);
        }

        String redisProbeValue = "unavailable";
        boolean redisAvailable = false;

        try {
            stringRedisTemplate.opsForValue().set(REDIS_PROBE_KEY, REDIS_PROBE_VALUE, Duration.ofMinutes(5));
            redisProbeValue = stringRedisTemplate.opsForValue().get(REDIS_PROBE_KEY);
            redisAvailable = REDIS_PROBE_VALUE.equals(redisProbeValue);
        } catch (Exception exception) {
            log.warn("Redis status check failed", exception);
        }

        String profiles = Arrays.toString(environment.getActiveProfiles());
        if (profiles.equals("[]")) {
            profiles = Arrays.toString(environment.getDefaultProfiles());
        }

        return new SystemStatusView(
                environment.getProperty("spring.application.name", "fpvhub"),
                profiles,
                databaseAvailable,
                databaseVersion,
                redisAvailable,
                redisProbeValue
        );
    }
}
