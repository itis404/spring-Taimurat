package ru.itis.fpvhub.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.itis.fpvhub.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalMvcExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalMvcExceptionHandler.class);

    @ExceptionHandler({
            ResourceNotFoundException.class,
            NoResourceFoundException.class,
            NoHandlerFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(Exception exception, HttpServletRequest request, Model model) {
        model.addAttribute("pageTitle", "404 — страница не найдена");
        model.addAttribute("activePage", "error");
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("statusCode", 404);
        return "error/404";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(AccessDeniedException exception, HttpServletRequest request, Model model) {
        model.addAttribute("pageTitle", "403 — доступ запрещён");
        model.addAttribute("activePage", "error");
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("statusCode", 403);
        return "error/403";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleUnexpected(Exception exception, HttpServletRequest request, Model model) {
        log.error("Unhandled MVC exception for {}", request.getRequestURI(), exception);
        model.addAttribute("pageTitle", "500 — ошибка сервера");
        model.addAttribute("activePage", "error");
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("statusCode", 500);
        return "error/500";
    }
}