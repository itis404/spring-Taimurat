package ru.itis.fpvhub.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.itis.fpvhub.dto.RegistrationResult;
import ru.itis.fpvhub.entity.EmailVerificationTokenEntity;
import ru.itis.fpvhub.entity.ProfileEntity;
import ru.itis.fpvhub.entity.RoleEntity;
import ru.itis.fpvhub.entity.UserEntity;
import ru.itis.fpvhub.entity.enums.RoleName;
import ru.itis.fpvhub.exception.RegistrationConflictException;
import ru.itis.fpvhub.form.RegistrationForm;
import ru.itis.fpvhub.repository.EmailVerificationTokenRepository;
import ru.itis.fpvhub.repository.RoleRepository;
import ru.itis.fpvhub.repository.UserRepository;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailVerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final DevEmailSender devEmailSender;
    private final ExternalEmailValidationService externalEmailValidationService;

    public RegistrationService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            EmailVerificationTokenRepository tokenRepository,
            PasswordEncoder passwordEncoder,
            DevEmailSender devEmailSender,
            ExternalEmailValidationService externalEmailValidationService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.devEmailSender = devEmailSender;
        this.externalEmailValidationService = externalEmailValidationService;
    }

    @Transactional
    public RegistrationResult register(RegistrationForm form) {
        String username = form.getUsername().trim();
        String email = form.getEmail().trim().toLowerCase();

        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new RegistrationConflictException("username", "Пользователь с таким username уже существует");
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new RegistrationConflictException("email", "Пользователь с таким email уже существует");
        }

        externalEmailValidationService.validateForRegistration(email);

        RoleEntity userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("ROLE_USER is not initialized"));

        UserEntity user = new UserEntity(username, email, passwordEncoder.encode(form.getPassword()));
        user.addRole(userRole);
        user.attachProfile(new ProfileEntity(form.getDisplayName().trim()));

        UserEntity savedUser = userRepository.save(user);

        String token = UUID.randomUUID().toString();
        EmailVerificationTokenEntity verificationToken = new EmailVerificationTokenEntity(
                savedUser,
                token,
                OffsetDateTime.now().plusHours(24)
        );
        tokenRepository.save(verificationToken);

        String verificationLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/email-verification/confirm")
                .queryParam("token", token)
                .build()
                .toUriString();

        devEmailSender.sendVerificationLink(email, verificationLink);
        return new RegistrationResult(email, verificationLink);
    }
}
