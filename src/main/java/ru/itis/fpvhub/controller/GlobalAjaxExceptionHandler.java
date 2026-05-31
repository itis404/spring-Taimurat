package ru.itis.fpvhub.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.itis.fpvhub.exception.ResourceNotFoundException;

import java.time.OffsetDateTime;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = {AjaxArticleReactionController.class, AjaxCommentController.class})
public class GlobalAjaxExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalAjaxExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody(404, "NOT_FOUND", exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpected(Exception exception) {
        log.error("Unhandled AJAX exception", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorBody(500, "INTERNAL_ERROR", "Unexpected server error"));
    }

    private Map<String, Object> errorBody(int status, String error, String message) {
        return Map.of(
                "status", status,
                "error", error,
                "message", message,
                "timestamp", OffsetDateTime.now().toString()
        );
    }
}
