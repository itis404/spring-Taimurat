package ru.itis.fpvhub.dto;

import ru.itis.fpvhub.entity.enums.ArticleReactionType;

public class ReactionResponse {
    private final long likes;
    private final long dislikes;
    private final ArticleReactionType currentUserReaction;

    public ReactionResponse(long likes, long dislikes, ArticleReactionType currentUserReaction) {
        this.likes = likes;
        this.dislikes = dislikes;
        this.currentUserReaction = currentUserReaction;
    }

    public long getLikes() {
        return likes;
    }

    public long getDislikes() {
        return dislikes;
    }

    public ArticleReactionType getCurrentUserReaction() {
        return currentUserReaction;
    }
}
