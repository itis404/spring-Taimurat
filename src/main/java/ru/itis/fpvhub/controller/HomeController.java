package ru.itis.fpvhub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.fpvhub.cache.PublicContentCacheService;

@Controller
public class HomeController {

    private final PublicContentCacheService publicContentCacheService;

    public HomeController(PublicContentCacheService publicContentCacheService) {
        this.publicContentCacheService = publicContentCacheService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "FPVHub — база знаний по FPV-квадрокоптерам");
        model.addAttribute("activePage", "home");
        model.addAttribute("latestArticles", publicContentCacheService.findLatestPublishedArticles());
        model.addAttribute("categories", publicContentCacheService.findCategoryOptions());
        return "pages/home";
    }
}
