package ru.itis.fpvhub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itis.fpvhub.cache.PublicContentCacheService;
import ru.itis.fpvhub.exception.ResourceNotFoundException;
import ru.itis.fpvhub.repository.CategoryRepository;
import ru.itis.fpvhub.service.ArticleService;

@Controller
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final ArticleService articleService;
    private final PublicContentCacheService publicContentCacheService;

    public CategoryController(
            CategoryRepository categoryRepository,
            ArticleService articleService,
            PublicContentCacheService publicContentCacheService
    ) {
        this.categoryRepository = categoryRepository;
        this.articleService = articleService;
        this.publicContentCacheService = publicContentCacheService;
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("pageTitle", "Категории — FPVHub");
        model.addAttribute("activePage", "categories");
        model.addAttribute("categories", publicContentCacheService.findCategoryOptions());
        return "category/list";
    }

    @GetMapping("/categories/{slug}")
    public String categoryDetails(@PathVariable("slug") String slug, Model model) {
        var category = categoryRepository.findBySlugForArticleList(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        model.addAttribute("pageTitle", category.getName() + " — FPVHub");
        model.addAttribute("activePage", "categories");
        model.addAttribute("category", category);
        model.addAttribute("articles", articleService.findPublishedByCategory(slug));
        return "category/details";
    }
}
