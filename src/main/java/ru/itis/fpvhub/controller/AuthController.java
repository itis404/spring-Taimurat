package ru.itis.fpvhub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageTitle", "Вход — FPVHub");
        model.addAttribute("activePage", "login");
        return "auth/login";
    }
}
