package ru.itis.fpvhub.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.fpvhub.converter.ArticleConverter;
import ru.itis.fpvhub.dto.CategoryOption;
import ru.itis.fpvhub.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ArticleConverter articleConverter;

    public CategoryService(CategoryRepository categoryRepository, ArticleConverter articleConverter) {
        this.categoryRepository = categoryRepository;
        this.articleConverter = articleConverter;
    }

    @Transactional(readOnly = true)
    public List<CategoryOption> findAllOptions() {
        return categoryRepository.findAllByOrderByNameAsc()
                .stream()
                .map(articleConverter::toCategoryOption)
                .toList();
    }
}
