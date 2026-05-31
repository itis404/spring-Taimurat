package ru.itis.fpvhub.exception;

public class RegistrationConflictException extends RuntimeException {
    private final String fieldName;

    public RegistrationConflictException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
