package ru.itis.fpvhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.fpvhub.entity.EmailVerificationTokenEntity;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationTokenEntity, Long> {

    @Query("""
            select distinct t from EmailVerificationTokenEntity t
            join fetch t.user u
            left join fetch u.roles
            where t.token = :token
            """)
    Optional<EmailVerificationTokenEntity> findByToken(@Param("token") String token);
}
