package ru.itis.fpvhub.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.fpvhub.dto.ArticleSearchResponse;
import ru.itis.fpvhub.form.ArticleSearchForm;
import ru.itis.fpvhub.service.ArticleService;

@RestController
public class AjaxArticleSearchController {

    private final ArticleService articleService;

    public AjaxArticleSearchController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/ajax/articles/search")
    public ArticleSearchResponse search(@ModelAttribute ArticleSearchForm form) {
        return new ArticleSearchResponse(articleService.searchPublishedArticles(form));
    }
}
