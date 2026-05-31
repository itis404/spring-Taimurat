package ru.itis.fpvhub.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DevEmailSender {

    private static final Logger log = LoggerFactory.getLogger(DevEmailSender.class);

    public void sendVerificationLink(String email, String verificationLink) {
        log.info("DEV email confirmation for {}: {}", email, verificationLink);
    }
}
