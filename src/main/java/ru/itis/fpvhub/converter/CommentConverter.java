package ru.itis.fpvhub.converter;

import org.springframework.stereotype.Component;
import ru.itis.fpvhub.dto.CommentView;
import ru.itis.fpvhub.entity.CommentEntity;

@Component
public class CommentConverter {

    public CommentView toView(CommentEntity comment, boolean canDelete) {
        String displayName = comment.getAuthor().getProfile() == null
                ? comment.getAuthor().getUsername()
                : comment.getAuthor().getProfile().getDisplayName();

        return new CommentView(
                comment.getId(),
                comment.getContent(),
                comment.isDeleted(),
                comment.getAuthor().getUsername(),
                displayName,
                comment.getCreatedAt(),
                canDelete
        );
    }
}
