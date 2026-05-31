package ru.itis.fpvhub.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import ru.itis.fpvhub.client.GithubOAuthClient;
import ru.itis.fpvhub.client.dto.GithubOAuthProfile;
import ru.itis.fpvhub.entity.OAuthAccountEntity;
import ru.itis.fpvhub.entity.ProfileEntity;
import ru.itis.fpvhub.entity.RoleEntity;
import ru.itis.fpvhub.entity.UserEntity;
import ru.itis.fpvhub.entity.enums.OAuthProvider;
import ru.itis.fpvhub.entity.enums.RoleName;
import ru.itis.fpvhub.repository.OAuthAccountRepository;
import ru.itis.fpvhub.repository.RoleRepository;
import ru.itis.fpvhub.repository.UserRepository;

import java.util.Locale;
import java.util.UUID;

@Service
public class GithubOAuthService {

    private static final Logger log = LoggerFactory.getLogger(GithubOAuthService.class);

    private final GithubOAuthClient githubOAuthClient;
    private final OAuthAccountRepository oauthAccountRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final boolean enabled;
    private final String clientId;
    private final String redirectUri;

    public GithubOAuthService(
            GithubOAuthClient githubOAuthClient,
            OAuthAccountRepository oauthAccountRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.oauth.github.enabled:false}") boolean enabled,
            @Value("${app.oauth.github.client-id:}") String clientId,
            @Value("${app.oauth.github.redirect-uri:http://localhost:8080/oauth2/github/callback}") String redirectUri
    ) {
        this.githubOAuthClient = githubOAuthClient;
        this.oauthAccountRepository = oauthAccountRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.enabled = enabled;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
    }

    public boolean isEnabled() {
        return enabled && clientId != null && !clientId.isBlank();
    }

    public String buildAuthorizationUrl(String state) {
        ensureConfigured();
        return UriComponentsBuilder.fromHttpUrl("https://github.com/login/oauth/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", "read:user user:email")
                .queryParam("state", state)
                .build()
                .toUriString();
    }

    @Transactional
    public UserEntity authenticate(String code) {
        ensureConfigured();
        String accessToken = githubOAuthClient.exchangeCodeForAccessToken(code, redirectUri);
        GithubOAuthProfile profile = githubOAuthClient.loadProfile(accessToken);

        return oauthAccountRepository.findDetailedByProviderAndProviderUserId(OAuthProvider.GITHUB, profile.id())
                .map(OAuthAccountEntity::getUser)
                .orElseGet(() -> createUserFromGithub(profile));
    }

    private UserEntity createUserFromGithub(GithubOAuthProfile profile) {
        RoleEntity userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("ROLE_USER is not initialized"));

        String username = makeUniqueUsername(profile.login());
        String email = makeEmail(profile);
        String displayName = makeDisplayName(profile);

        ProfileEntity localProfile = new ProfileEntity(displayName);
        localProfile.setAvatarUrl(profile.avatarUrl());

        UserEntity user = new UserEntity(username, email, passwordEncoder.encode(UUID.randomUUID().toString()));
        user.addRole(userRole);
        user.attachProfile(localProfile);
        user.verifyEmail();

        UserEntity savedUser = userRepository.save(user);
        oauthAccountRepository.save(new OAuthAccountEntity(
                savedUser,
                OAuthProvider.GITHUB,
                profile.id(),
                profile.login(),
                profile.email()
        ));

        log.info("Created local user {} from GitHub OAuth account {}", username, profile.login());
        return savedUser;
    }

    private String makeUniqueUsername(String githubLogin) {
        String base = githubLogin == null || githubLogin.isBlank() ? "github-user" : githubLogin;
        base = base.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_\\-]", "-");
        if (base.length() > 32) {
            base = base.substring(0, 32);
        }
        if (base.length() < 3) {
            base = "github-" + base;
        }

        String candidate = base;
        int suffix = 1;
        while (userRepository.existsByUsernameIgnoreCase(candidate)) {
            String ending = "-" + suffix++;
            int maxBaseLength = Math.min(base.length(), 40 - ending.length());
            candidate = base.substring(0, maxBaseLength) + ending;
        }
        return candidate;
    }

    private String makeEmail(GithubOAuthProfile profile) {
        if (profile.email() != null && !profile.email().isBlank() && !userRepository.existsByEmailIgnoreCase(profile.email())) {
            return profile.email().toLowerCase(Locale.ROOT);
        }
        return "github-" + profile.id() + "@users.noreply.fpvhub.local";
    }

    private String makeDisplayName(GithubOAuthProfile profile) {
        if (profile.name() != null && !profile.name().isBlank()) {
            return limit(profile.name(), 80);
        }
        return limit(profile.login(), 80);
    }

    private String limit(String value, int limit) {
        if (value == null || value.isBlank()) {
            return "GitHub User";
        }
        return value.length() <= limit ? value : value.substring(0, limit);
    }

    private void ensureConfigured() {
        if (!isEnabled()) {
            throw new IllegalStateException("GitHub OAuth is disabled or not configured");
        }
    }
}
