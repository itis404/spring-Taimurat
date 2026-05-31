package ru.itis.fpvhub.dto.cache;

import ru.itis.fpvhub.dto.TagOption;

public record CachedTagOption(
        Long id,
        String name,
        String slug
) {

    public static CachedTagOption from(TagOption source) {
        return new CachedTagOption(source.getId(), source.getName(), source.getSlug());
    }

    public TagOption toView() {
        return new TagOption(id, name, slug);
    }
}
