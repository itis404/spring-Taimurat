package ru.itis.fpvhub.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.fpvhub.entity.ArticleEntity;
import ru.itis.fpvhub.form.ArticleForm;
import ru.itis.fpvhub.form.CommentForm;
import ru.itis.fpvhub.form.ArticleSearchForm;
import ru.itis.fpvhub.cache.PublicContentCacheService;
import ru.itis.fpvhub.service.ArticleReactionService;
import ru.itis.fpvhub.service.ArticleService;
import ru.itis.fpvhub.service.CommentService;

@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final CommentService commentService;
    private final ArticleReactionService articleReactionService;
    private final PublicContentCacheService publicContentCacheService;

    public ArticleController(
            ArticleService articleService,
            CommentService commentService,
            ArticleReactionService articleReactionService,
            PublicContentCacheService publicContentCacheService
    ) {
        this.articleService = articleService;
        this.commentService = commentService;
        this.articleReactionService = articleReactionService;
        this.publicContentCacheService = publicContentCacheService;
    }

    @GetMapping("/articles")
    public String list(@ModelAttribute("searchForm") ArticleSearchForm searchForm, Model model) {
        model.addAttribute("pageTitle", "Статьи — FPVHub");
        model.addAttribute("activePage", "articles");
        model.addAttribute("articles", articleService.searchPublishedArticles(searchForm));
        model.addAttribute("categories", publicContentCacheService.findCategoryOptions());
        model.addAttribute("tags", articleService.getTagOptions());
        model.addAttribute("articleStatistics", publicContentCacheService.getArticleStatistics());
        return "article/list";
    }

    @GetMapping("/articles/{slug}")
    public String details(@PathVariable("slug") String slug, Authentication authentication, Model model) {
        var article = articleService.getPublishedDetails(slug, authentication);
        model.addAttribute("article", article);
        model.addAttribute("comments", commentService.findByArticle(article.getId(), authentication));
        model.addAttribute("commentForm", new CommentForm());
        model.addAttribute("reactionSummary", articleReactionService.getSummary(article.getId(), authentication));
        model.addAttribute("activePage", "articles");
        model.addAttribute("pageTitle", "Статья — FPVHub");
        return "article/details";
    }

    @GetMapping("/articles/new")
    public String createForm(Model model) {
        prepareArticleForm(model, articleService.createEmptyForm(), "Новая статья — FPVHub");
        return "article/create";
    }

    @PostMapping("/articles")
    public String create(
            @Valid @ModelAttribute("articleForm") ArticleForm form,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            prepareArticleForm(model, form, "Новая статья — FPVHub");
            return "article/create";
        }

        ArticleEntity article = articleService.create(authentication, form);
        redirectAttributes.addFlashAttribute("successMessage", "Статья сохранена. Статус: " + article.getStatus().getDisplayName());
        return "redirect:/my/articles";
    }

    @GetMapping("/articles/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Authentication authentication, Model model) {
        ArticleForm form = articleService.getFormForEdit(id, authentication);
        model.addAttribute("articleId", id);
        prepareArticleForm(model, form, "Редактирование статьи — FPVHub");
        return "article/edit";
    }

    @PostMapping("/articles/{id}/edit")
    public String update(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("articleForm") ArticleForm form,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("articleId", id);
            prepareArticleForm(model, form, "Редактирование статьи — FPVHub");
            return "article/edit";
        }

        ArticleEntity article = articleService.update(id, authentication, form);
        redirectAttributes.addFlashAttribute("successMessage", "Статья обновлена. Статус: " + article.getStatus().getDisplayName());
        return "redirect:/my/articles";
    }

    @PostMapping("/articles/{id}/delete")
    public String archive(@PathVariable("id") Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        articleService.archive(id, authentication);
        redirectAttributes.addFlashAttribute("successMessage", "Статья архивирована");
        return "redirect:/my/articles";
    }

    @GetMapping("/my/articles")
    public String myArticles(Authentication authentication, Model model) {
        model.addAttribute("pageTitle", "Мои статьи — FPVHub");
        model.addAttribute("activePage", "myArticles");
        model.addAttribute("articles", articleService.findCurrentUserArticles(authentication));
        return "article/my-articles";
    }

    private void prepareArticleForm(Model model, ArticleForm form, String pageTitle) {
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("activePage", "articles");
        if (!model.containsAttribute("articleForm")) {
            model.addAttribute("articleForm", form);
        }
        model.addAttribute("categories", publicContentCacheService.findCategoryOptions());
        model.addAttribute("tags", articleService.getTagOptions());
    }
}
