package ru.itis.fpvhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.fpvhub.entity.ProfileEntity;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
}
