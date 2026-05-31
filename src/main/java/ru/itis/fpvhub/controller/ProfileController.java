package ru.itis.fpvhub.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.fpvhub.entity.UserEntity;
import ru.itis.fpvhub.form.ProfileUpdateForm;
import ru.itis.fpvhub.service.UserService;

@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        UserEntity user = userService.getCurrentUser(authentication);
        model.addAttribute("pageTitle", "Профиль — FPVHub");
        model.addAttribute("activePage", "profile");
        model.addAttribute("user", user);
        if (!model.containsAttribute("profileUpdateForm")) {
            model.addAttribute("profileUpdateForm", userService.getCurrentUserProfileForm(authentication));
        }
        return "profile/my-profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            Authentication authentication,
            @Valid @ModelAttribute("profileUpdateForm") ProfileUpdateForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            UserEntity user = userService.getCurrentUser(authentication);
            model.addAttribute("pageTitle", "Профиль — FPVHub");
            model.addAttribute("activePage", "profile");
            model.addAttribute("user", user);
            return "profile/my-profile";
        }

        userService.updateCurrentUserProfile(authentication, form);
        redirectAttributes.addFlashAttribute("profileUpdated", true);
        return "redirect:/profile";
    }
}
