package ru.itis.fpvhub.dto;

import java.time.OffsetDateTime;

public class CommentView {
    private final Long id;
    private final String content;
    private final boolean deleted;
    private final String authorUsername;
    private final String authorDisplayName;
    private final OffsetDateTime createdAt;
    private final boolean canDelete;

    public CommentView(
            Long id,
            String content,
            boolean deleted,
            String authorUsername,
            String authorDisplayName,
            OffsetDateTime createdAt,
            boolean canDelete
    ) {
        this.id = id;
        this.content = content;
        this.deleted = deleted;
        this.authorUsername = authorUsername;
        this.authorDisplayName = authorDisplayName;
        this.createdAt = createdAt;
        this.canDelete = canDelete;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public String getAuthorDisplayName() {
        return authorDisplayName;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isCanDelete() {
        return canDelete;
    }
}
