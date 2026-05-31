package ru.itis.fpvhub.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.fpvhub.dto.DeleteCommentResponse;
import ru.itis.fpvhub.service.CommentService;

@RestController
public class AjaxCommentController {

    private final CommentService commentService;

    public AjaxCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/ajax/comments/{commentId}/delete")
    public DeleteCommentResponse delete(@PathVariable("commentId") Long commentId, Authentication authentication) {
        commentService.delete(commentId, authentication);
        return new DeleteCommentResponse(true, commentId);
    }
}
