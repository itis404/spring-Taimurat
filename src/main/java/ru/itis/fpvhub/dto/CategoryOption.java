package ru.itis.fpvhub.dto;

public class CategoryOption {
    private final Long id;
    private final String name;
    private final String slug;
    private final String description;

    public CategoryOption(Long id, String name, String slug, String description) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getDescription() {
        return description;
    }
}
