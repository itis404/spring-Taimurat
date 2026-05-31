package ru.itis.fpvhub.client.dto;

public record GithubOAuthProfile(
        String id,
        String login,
        String name,
        String email,
        String avatarUrl
) {
}
