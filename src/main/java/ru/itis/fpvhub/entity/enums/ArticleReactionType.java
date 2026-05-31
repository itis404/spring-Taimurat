package ru.itis.fpvhub.entity.enums;

public enum ArticleReactionType {
    LIKE("Лайк"),
    DISLIKE("Дизлайк");

    private final String displayName;

    ArticleReactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
