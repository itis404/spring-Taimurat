package ru.itis.fpvhub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.fpvhub.service.ArticleService;

@Controller
public class AdminArticleController {

    private final ArticleService articleService;

    public AdminArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/admin/articles")
    public String articles(Model model) {
        model.addAttribute("pageTitle", "Модерация статей — FPVHub");
        model.addAttribute("activePage", "admin");
        model.addAttribute("articles", articleService.findAllForAdmin());
        return "admin/articles";
    }

    @PostMapping("/admin/articles/{id}/publish")
    public String publish(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        articleService.publish(id);
        redirectAttributes.addFlashAttribute("successMessage", "Статья опубликована");
        return "redirect:/admin/articles";
    }

    @PostMapping("/admin/articles/{id}/reject")
    public String reject(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        articleService.reject(id);
        redirectAttributes.addFlashAttribute("successMessage", "Статья отклонена");
        return "redirect:/admin/articles";
    }

    @PostMapping("/admin/articles/{id}/archive")
    public String archive(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        articleService.archiveByAdmin(id);
        redirectAttributes.addFlashAttribute("successMessage", "Статья архивирована");
        return "redirect:/admin/articles";
    }
}
