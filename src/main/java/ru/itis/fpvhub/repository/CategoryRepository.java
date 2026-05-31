package ru.itis.fpvhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.fpvhub.entity.CategoryEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findBySlug(String slug);

    Optional<CategoryEntity> findByNameIgnoreCase(String name);

    boolean existsBySlug(String slug);

    List<CategoryEntity> findAllByOrderByNameAsc();

    @Query("""
            select c from CategoryEntity c
            where c.slug = :slug
            """)
    Optional<CategoryEntity> findBySlugForArticleList(@Param("slug") String slug);
}
