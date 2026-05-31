package ru.itis.fpvhub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.fpvhub.dto.VerificationResult;
import ru.itis.fpvhub.service.EmailVerificationService;

@Controller
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    public EmailVerificationController(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    @GetMapping("/email-verification/confirm")
    public String confirm(@RequestParam("token") String token, Model model) {
        VerificationResult result = emailVerificationService.verify(token);
        model.addAttribute("pageTitle", result.getTitle() + " — FPVHub");
        model.addAttribute("activePage", "login");
        model.addAttribute("verificationResult", result);
        return "auth/email-verification-result";
    }
}
