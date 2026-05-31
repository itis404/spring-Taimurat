package ru.itis.fpvhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.fpvhub.entity.TagEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagRepository extends JpaRepository<TagEntity, Long> {

    Optional<TagEntity> findBySlug(String slug);

    Optional<TagEntity> findByNameIgnoreCase(String name);

    boolean existsBySlug(String slug);

    List<TagEntity> findAllByOrderByNameAsc();

    Set<TagEntity> findAllByIdIn(Collection<Long> ids);
}
