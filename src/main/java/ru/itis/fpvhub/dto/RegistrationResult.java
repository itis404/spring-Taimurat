package ru.itis.fpvhub.dto;

public record RegistrationResult(
        String email,
        String verificationLink
) {
}
