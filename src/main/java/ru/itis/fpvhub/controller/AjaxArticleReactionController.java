package ru.itis.fpvhub.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.fpvhub.dto.ReactionResponse;
import ru.itis.fpvhub.entity.enums.ArticleReactionType;
import ru.itis.fpvhub.service.ArticleReactionService;

@RestController
public class AjaxArticleReactionController {

    private final ArticleReactionService articleReactionService;

    public AjaxArticleReactionController(ArticleReactionService articleReactionService) {
        this.articleReactionService = articleReactionService;
    }

    @PostMapping("/ajax/articles/{articleId}/reaction")
    public ReactionResponse react(
            @PathVariable("articleId") Long articleId,
            @RequestParam("type") ArticleReactionType type,
            Authentication authentication
    ) {
        return articleReactionService.react(articleId, type, authentication);
    }
}
