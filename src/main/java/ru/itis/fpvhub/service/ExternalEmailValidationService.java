package ru.itis.fpvhub.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itis.fpvhub.client.EmailValidationApiClient;
import ru.itis.fpvhub.client.dto.EmailValidationResult;
import ru.itis.fpvhub.exception.RegistrationConflictException;

@Service
public class ExternalEmailValidationService {

    private static final Logger log = LoggerFactory.getLogger(ExternalEmailValidationService.class);

    private final EmailValidationApiClient emailValidationApiClient;
    private final boolean enabled;
    private final boolean failOpen;

    public ExternalEmailValidationService(
            EmailValidationApiClient emailValidationApiClient,
            @Value("${app.email-validation.enabled:false}") boolean enabled,
            @Value("${app.email-validation.fail-open:true}") boolean failOpen
    ) {
        this.emailValidationApiClient = emailValidationApiClient;
        this.enabled = enabled;
        this.failOpen = failOpen;
    }

    public void validateForRegistration(String email) {
        if (!enabled) {
            log.debug("External email validation is disabled. Email {} accepted after local validation", email);
            return;
        }

        try {
            EmailValidationResult result = emailValidationApiClient.validate(email);
            log.info("Email validation result for {}: valid={}, deliverability={}, mxFound={}, disposable={}",
                    email, result.valid(), result.deliverability(), result.mxFound(), result.disposable());

            if (!result.valid()) {
                throw new RegistrationConflictException("email", result.message());
            }
        } catch (RegistrationConflictException exception) {
            throw exception;
        } catch (Exception exception) {
            log.warn("External email validation failed for {}", email, exception);
            if (!failOpen) {
                throw new RegistrationConflictException("email", "Сервис внешней проверки email временно недоступен");
            }
        }
    }
}
