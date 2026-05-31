package ru.itis.fpvhub.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.fpvhub.form.CommentForm;
import ru.itis.fpvhub.service.CommentService;

@Controller
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/articles/{articleId}/comments")
    public String create(
            @PathVariable("articleId") Long articleId,
            @Valid @ModelAttribute("commentForm") CommentForm form,
            BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Комментарий не должен быть пустым и длиннее 2000 символов");
            return "redirect:/articles";
        }

        String articleSlug = commentService.create(articleId, form, authentication);
        redirectAttributes.addFlashAttribute("successMessage", "Комментарий добавлен");
        return "redirect:/articles/" + articleSlug + "#comments";
    }

    @PostMapping("/comments/{commentId}/delete")
    public String delete(
            @PathVariable("commentId") Long commentId,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        String articleSlug = commentService.delete(commentId, authentication);
        redirectAttributes.addFlashAttribute("successMessage", "Комментарий удалён");
        return "redirect:/articles/" + articleSlug + "#comments";
    }
}
