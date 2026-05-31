package ru.itis.fpvhub.dto;

public class DeleteCommentResponse {
    private final boolean deleted;
    private final Long commentId;

    public DeleteCommentResponse(boolean deleted, Long commentId) {
        this.deleted = deleted;
        this.commentId = commentId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Long getCommentId() {
        return commentId;
    }
}
