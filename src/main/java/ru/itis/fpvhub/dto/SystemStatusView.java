package ru.itis.fpvhub.dto;

public class SystemStatusView {

    private final String applicationName;
    private final String activeProfiles;
    private final boolean databaseAvailable;
    private final String databaseVersion;
    private final boolean redisAvailable;
    private final String redisProbeValue;

    public SystemStatusView(
            String applicationName,
            String activeProfiles,
            boolean databaseAvailable,
            String databaseVersion,
            boolean redisAvailable,
            String redisProbeValue
    ) {
        this.applicationName = applicationName;
        this.activeProfiles = activeProfiles;
        this.databaseAvailable = databaseAvailable;
        this.databaseVersion = databaseVersion;
        this.redisAvailable = redisAvailable;
        this.redisProbeValue = redisProbeValue;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getActiveProfiles() {
        return activeProfiles;
    }

    public boolean isDatabaseAvailable() {
        return databaseAvailable;
    }

    public String getDatabaseVersion() {
        return databaseVersion;
    }

    public boolean isRedisAvailable() {
        return redisAvailable;
    }

    public String getRedisProbeValue() {
        return redisProbeValue;
    }
}
