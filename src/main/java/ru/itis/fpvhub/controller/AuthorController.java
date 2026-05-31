package ru.itis.fpvhub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itis.fpvhub.exception.ResourceNotFoundException;
import ru.itis.fpvhub.repository.UserRepository;
import ru.itis.fpvhub.service.ArticleService;

@Controller
public class AuthorController {

    private final UserRepository userRepository;
    private final ArticleService articleService;

    public AuthorController(UserRepository userRepository, ArticleService articleService) {
        this.userRepository = userRepository;
        this.articleService = articleService;
    }

    @GetMapping("/authors/{username}")
    public String author(@PathVariable("username") String username, Model model) {
        var user = userRepository.findPublicProfileByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        model.addAttribute("pageTitle", user.getUsername() + " — FPVHub");
        model.addAttribute("activePage", "authors");
        model.addAttribute("author", user);
        model.addAttribute("articles", articleService.findPublishedByAuthor(user.getUsername()));
        return "author/details";
    }
}
