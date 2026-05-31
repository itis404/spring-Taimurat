package ru.itis.fpvhub.client.dto;

public record EmailValidationResult(
        boolean valid,
        String message,
        String deliverability,
        boolean disposable,
        boolean mxFound,
        boolean validFormat,
        String rawResponse
) {
}
