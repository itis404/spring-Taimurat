package ru.itis.fpvhub.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.fpvhub.entity.UserEntity;
import ru.itis.fpvhub.security.CustomUserDetailsService;
import ru.itis.fpvhub.service.GithubOAuthService;

import java.util.UUID;

@Controller
public class GithubOAuthController {

    public static final String GITHUB_OAUTH_STATE_SESSION_KEY = "GITHUB_OAUTH_STATE";

    private final GithubOAuthService githubOAuthService;
    private final CustomUserDetailsService userDetailsService;

    public GithubOAuthController(GithubOAuthService githubOAuthService, CustomUserDetailsService userDetailsService) {
        this.githubOAuthService = githubOAuthService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/oauth2/github/start")
    public String start(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!githubOAuthService.isEnabled()) {
            redirectAttributes.addFlashAttribute("oauthError", "GitHub OAuth не настроен.");
            return "redirect:/login";
        }
        String state = UUID.randomUUID().toString();
        session.setAttribute(GITHUB_OAUTH_STATE_SESSION_KEY, state);
        return "redirect:" + githubOAuthService.buildAuthorizationUrl(state);
    }

    @GetMapping("/oauth2/github/callback")
    public String callback(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "error", required = false) String error,
            HttpServletRequest request,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (error != null) {
            redirectAttributes.addFlashAttribute("oauthError", "GitHub OAuth вернул ошибку: " + error);
            return "redirect:/login";
        }
        if (code == null || code.isBlank()) {
            redirectAttributes.addFlashAttribute("oauthError", "GitHub OAuth не вернул authorization code");
            return "redirect:/login";
        }

        String expectedState = (String) session.getAttribute(GITHUB_OAUTH_STATE_SESSION_KEY);
        session.removeAttribute(GITHUB_OAUTH_STATE_SESSION_KEY);
        if (expectedState == null || state == null || !expectedState.equals(state)) {
            redirectAttributes.addFlashAttribute("oauthError", "Некорректный OAuth state. Авторизация отменена.");
            return "redirect:/login";
        }

        try {
            UserEntity user = githubOAuthService.authenticate(code);
            authenticateInCurrentSession(user.getUsername(), request);
            return "redirect:/?oauth=github";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("oauthError", "Не удалось войти через GitHub: " + exception.getMessage());
            return "redirect:/login";
        }
    }

    private void authenticateInCurrentSession(String username, HttpServletRequest request) {
        UserDetails principal = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal,
                principal.getPassword(),
                principal.getAuthorities()
        );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
    }
}
