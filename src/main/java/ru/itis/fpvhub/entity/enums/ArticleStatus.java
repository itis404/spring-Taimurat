package ru.itis.fpvhub.entity.enums;

public enum ArticleStatus {
    DRAFT("Черновик"),
    PENDING_REVIEW("На модерации"),
    PUBLISHED("Опубликована"),
    REJECTED("Отклонена"),
    ARCHIVED("Архивирована");

    private final String displayName;

    ArticleStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
