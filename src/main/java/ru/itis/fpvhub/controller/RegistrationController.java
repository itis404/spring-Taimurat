package ru.itis.fpvhub.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.fpvhub.dto.RegistrationResult;
import ru.itis.fpvhub.exception.RegistrationConflictException;
import ru.itis.fpvhub.form.RegistrationForm;
import ru.itis.fpvhub.service.RegistrationService;
import ru.itis.fpvhub.service.UserService;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;
    private final UserService userService;

    public RegistrationController(RegistrationService registrationService, UserService userService) {
        this.registrationService = registrationService;
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("pageTitle", "Регистрация — FPVHub");
        model.addAttribute("activePage", "registration");
        if (!model.containsAttribute("registrationForm")) {
            model.addAttribute("registrationForm", new RegistrationForm());
        }
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String register(
            @Valid @ModelAttribute("registrationForm") RegistrationForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        validateForm(form, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Регистрация — FPVHub");
            model.addAttribute("activePage", "registration");
            return "auth/registration";
        }

        try {
            RegistrationResult result = registrationService.register(form);
            redirectAttributes.addFlashAttribute("email", result.email());
            redirectAttributes.addFlashAttribute("verificationLink", result.verificationLink());
            return "redirect:/registration/success";
        } catch (RegistrationConflictException exception) {
            bindingResult.rejectValue(exception.getFieldName(), "registration.conflict", exception.getMessage());
            model.addAttribute("pageTitle", "Регистрация — FPVHub");
            model.addAttribute("activePage", "registration");
            return "auth/registration";
        }
    }

    @GetMapping("/registration/success")
    public String registrationSuccess(Model model) {
        model.addAttribute("pageTitle", "Подтверждение email — FPVHub");
        model.addAttribute("activePage", "registration");
        return "auth/registration-success";
    }

    private void validateForm(RegistrationForm form, BindingResult bindingResult) {
        if (form.getPassword() != null && form.getPasswordRepeat() != null
                && !form.getPassword().equals(form.getPasswordRepeat())) {
            bindingResult.rejectValue("passwordRepeat", "password.mismatch", "Пароли не совпадают");
        }
        if (form.getUsername() != null && userService.existsByUsername(form.getUsername())) {
            bindingResult.rejectValue("username", "username.exists", "Пользователь с таким username уже существует");
        }
        if (form.getEmail() != null && userService.existsByEmail(form.getEmail())) {
            bindingResult.rejectValue("email", "email.exists", "Пользователь с таким email уже существует");
        }
    }
}
