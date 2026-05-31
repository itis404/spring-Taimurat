package ru.itis.fpvhub.dto;

public class VerificationResult {
    private final boolean success;
    private final String title;
    private final String message;

    private VerificationResult(boolean success, String title, String message) {
        this.success = success;
        this.title = title;
        this.message = message;
    }

    public static VerificationResult success() {
        return new VerificationResult(true, "Email подтверждён", "Аккаунт активирован. Теперь можно войти в систему.");
    }

    public static VerificationResult invalidToken() {
        return new VerificationResult(false, "Некорректная ссылка", "Токен подтверждения не найден.");
    }

    public static VerificationResult expiredToken() {
        return new VerificationResult(false, "Срок действия ссылки истёк", "Зарегистрируйтесь заново или запросите новую ссылку подтверждения позже.");
    }

    public static VerificationResult alreadyUsed() {
        return new VerificationResult(false, "Ссылка уже использована", "Этот email уже был подтверждён ранее.");
    }

    public boolean isSuccess() {
        return success;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
