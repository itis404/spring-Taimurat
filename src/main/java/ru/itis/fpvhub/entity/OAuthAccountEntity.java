package ru.itis.fpvhub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import ru.itis.fpvhub.entity.enums.OAuthProvider;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "oauth_accounts",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_oauth_accounts_provider_user", columnNames = {"provider", "provider_user_id"})
        }
)
public class OAuthAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, length = 40)
    private OAuthProvider provider;

    @Column(name = "provider_user_id", nullable = false, length = 120)
    private String providerUserId;

    @Column(name = "provider_username", nullable = false, length = 120)
    private String providerUsername;

    @Column(name = "provider_email", length = 254)
    private String providerEmail;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected OAuthAccountEntity() {
    }

    public OAuthAccountEntity(
            UserEntity user,
            OAuthProvider provider,
            String providerUserId,
            String providerUsername,
            String providerEmail
    ) {
        this.user = user;
        this.provider = provider;
        this.providerUserId = providerUserId;
        this.providerUsername = providerUsername;
        this.providerEmail = providerEmail;
    }

    @PrePersist
    void prePersist() {
        this.createdAt = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public OAuthProvider getProvider() {
        return provider;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public String getProviderUsername() {
        return providerUsername;
    }

    public String getProviderEmail() {
        return providerEmail;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
