package ru.itis.fpvhub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.fpvhub.cache.CacheInvalidationService;
import ru.itis.fpvhub.entity.enums.ArticleStatus;
import ru.itis.fpvhub.repository.ArticleRepository;
import ru.itis.fpvhub.repository.CommentRepository;
import ru.itis.fpvhub.repository.OAuthAccountRepository;
import ru.itis.fpvhub.repository.UserRepository;

@Controller
public class AdminController {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final OAuthAccountRepository oauthAccountRepository;
    private final CacheInvalidationService cacheInvalidationService;

    public AdminController(
            UserRepository userRepository,
            ArticleRepository articleRepository,
            CommentRepository commentRepository,
            OAuthAccountRepository oauthAccountRepository,
            CacheInvalidationService cacheInvalidationService
    ) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.oauthAccountRepository = oauthAccountRepository;
        this.cacheInvalidationService = cacheInvalidationService;
    }

    @GetMapping("/admin")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Админ-панель — FPVHub");
        model.addAttribute("activePage", "admin");
        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("publishedArticleCount", articleRepository.countByStatus(ArticleStatus.PUBLISHED));
        model.addAttribute("pendingArticleCount", articleRepository.countByStatus(ArticleStatus.PENDING_REVIEW));
        model.addAttribute("commentCount", commentRepository.countByDeletedFalse());
        model.addAttribute("oauthAccountCount", oauthAccountRepository.count());
        return "admin/dashboard";
    }

    @PostMapping("/admin/cache/evict")
    public String evictCache(RedirectAttributes redirectAttributes) {
        cacheInvalidationService.evictPublicContentCaches();
        redirectAttributes.addFlashAttribute("successMessage", "Публичный Redis-кэш сброшен");
        return "redirect:/admin";
    }
}
