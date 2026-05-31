package ru.itis.fpvhub.dto.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.Map;

@Schema(description = "Unified JSON error response for REST API")
public class ApiErrorResponse {

    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final OffsetDateTime timestamp;
    private final Map<String, String> fieldErrors;

    public ApiErrorResponse(int status, String error, String message, String path, Map<String, String> fieldErrors) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = OffsetDateTime.now();
        this.fieldErrors = fieldErrors;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
