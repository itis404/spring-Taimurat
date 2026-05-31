package ru.itis.fpvhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.fpvhub.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    Optional<UserEntity> findByUsernameIgnoreCase(String username);

    Optional<UserEntity> findByEmailIgnoreCase(String email);

    @Query("""
            select distinct u from UserEntity u
            left join fetch u.roles
            where lower(u.username) = lower(:login) or lower(u.email) = lower(:login)
            """)
    Optional<UserEntity> findForAuthentication(@Param("login") String login);

    @Query("""
            select u from UserEntity u
            left join fetch u.profile
            where lower(u.username) = lower(:username)
            """)
    Optional<UserEntity> findPublicProfileByUsername(@Param("username") String username);

    @Query("""
            select distinct u from UserEntity u
            left join fetch u.profile
            left join fetch u.roles
            where u.id = :id
            """)
    Optional<UserEntity> findDetailedById(@Param("id") Long id);
}
