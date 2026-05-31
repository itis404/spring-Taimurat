package ru.itis.fpvhub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.fpvhub.service.SystemStatusService;

@Controller
@RequiredArgsConstructor
public class SystemController {

    private final SystemStatusService systemStatusService;

    @GetMapping("/system/status")
    public String status(Model model) {
        model.addAttribute("pageTitle", "System status");
        model.addAttribute("activePage", "system");
        model.addAttribute("status", systemStatusService.getStatus());
        return "system/status";
    }
}
