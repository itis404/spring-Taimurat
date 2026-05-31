package ru.itis.fpvhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.fpvhub.entity.RoleEntity;
import ru.itis.fpvhub.entity.enums.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(RoleName name);
}
