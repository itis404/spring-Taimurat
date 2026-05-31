package ru.itis.fpvhub.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.fpvhub.converter.ProfileConverter;
import ru.itis.fpvhub.entity.UserEntity;
import ru.itis.fpvhub.exception.ResourceNotFoundException;
import ru.itis.fpvhub.form.ProfileUpdateForm;
import ru.itis.fpvhub.repository.UserRepository;
import ru.itis.fpvhub.security.CustomUserPrincipal;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ProfileConverter profileConverter;

    public UserService(UserRepository userRepository, ProfileConverter profileConverter) {
        this.userRepository = userRepository;
        this.profileConverter = profileConverter;
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return username != null && userRepository.existsByUsernameIgnoreCase(username.trim());
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return email != null && userRepository.existsByEmailIgnoreCase(email.trim());
    }

    @Transactional(readOnly = true)
    public UserEntity getCurrentUser(Authentication authentication) {
        Long userId = extractUserId(authentication);
        return userRepository.findDetailedById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }

    @Transactional(readOnly = true)
    public ProfileUpdateForm getCurrentUserProfileForm(Authentication authentication) {
        UserEntity user = getCurrentUser(authentication);
        return profileConverter.toForm(user.getProfile());
    }

    @Transactional
    public void updateCurrentUserProfile(Authentication authentication, ProfileUpdateForm form) {
        UserEntity user = getCurrentUser(authentication);
        profileConverter.updateEntity(user.getProfile(), form);
    }

    private Long extractUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserPrincipal customUserPrincipal) {
            return customUserPrincipal.getId();
        }
        throw new IllegalStateException("Unsupported principal type: " + principal.getClass().getName());
    }
}
