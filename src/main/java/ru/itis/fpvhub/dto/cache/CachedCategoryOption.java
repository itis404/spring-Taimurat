package ru.itis.fpvhub.dto.cache;

import ru.itis.fpvhub.dto.CategoryOption;

public record CachedCategoryOption(
        Long id,
        String name,
        String slug,
        String description
) {

    public static CachedCategoryOption from(CategoryOption source) {
        return new CachedCategoryOption(
                source.getId(),
                source.getName(),
                source.getSlug(),
                source.getDescription()
        );
    }

    public CategoryOption toView() {
        return new CategoryOption(id, name, slug, description);
    }
}
