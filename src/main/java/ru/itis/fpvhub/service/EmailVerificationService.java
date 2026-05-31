package ru.itis.fpvhub.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.fpvhub.dto.VerificationResult;
import ru.itis.fpvhub.repository.EmailVerificationTokenRepository;

@Service
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;

    public EmailVerificationService(EmailVerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public VerificationResult verify(String token) {
        return tokenRepository.findByToken(token)
                .map(verificationToken -> {
                    if (verificationToken.isUsed()) {
                        return VerificationResult.alreadyUsed();
                    }
                    if (verificationToken.isExpired()) {
                        return VerificationResult.expiredToken();
                    }

                    verificationToken.getUser().verifyEmail();
                    verificationToken.markUsed();
                    return VerificationResult.success();
                })
                .orElseGet(VerificationResult::invalidToken);
    }
}
