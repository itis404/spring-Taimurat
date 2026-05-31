package ru.itis.fpvhub.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object errorPath = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        int status = statusCode == null
                ? HttpStatus.INTERNAL_SERVER_ERROR.value()
                : Integer.parseInt(statusCode.toString());

        String path = errorPath == null
                ? request.getRequestURI()
                : errorPath.toString();

        model.addAttribute("status", status);
        model.addAttribute("path", path);

        if (status == HttpStatus.NOT_FOUND.value()) {
            return "error/404";
        }

        if (status == HttpStatus.FORBIDDEN.value()) {
            return "error/403";
        }

        return "error/500";
    }

    @RequestMapping("/error/404")
    public String notFound(HttpServletRequest request, Model model) {
        model.addAttribute("status", 404);
        model.addAttribute("path", request.getRequestURI());
        return "error/404";
    }

    @RequestMapping("/error/403")
    public String forbidden(HttpServletRequest request, Model model) {
        model.addAttribute("status", 403);
        model.addAttribute("path", request.getRequestURI());
        return "error/403";
    }

    @RequestMapping("/error/500")
    public String serverError(HttpServletRequest request, Model model) {
        model.addAttribute("status", 500);
        model.addAttribute("path", request.getRequestURI());
        return "error/500";
    }
}