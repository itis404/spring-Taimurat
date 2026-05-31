package ru.itis.fpvhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.fpvhub.entity.OAuthAccountEntity;
import ru.itis.fpvhub.entity.enums.OAuthProvider;

import java.util.Optional;

public interface OAuthAccountRepository extends JpaRepository<OAuthAccountEntity, Long> {

    @Query("""
            select oa from OAuthAccountEntity oa
            join fetch oa.user u
            left join fetch u.roles
            where oa.provider = :provider and oa.providerUserId = :providerUserId
            """)
    Optional<OAuthAccountEntity> findDetailedByProviderAndProviderUserId(
            @Param("provider") OAuthProvider provider,
            @Param("providerUserId") String providerUserId
    );
}
