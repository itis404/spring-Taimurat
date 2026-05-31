package ru.itis.fpvhub.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.fpvhub.converter.ArticleConverter;
import ru.itis.fpvhub.dto.TagOption;
import ru.itis.fpvhub.repository.TagRepository;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final ArticleConverter articleConverter;

    public TagService(TagRepository tagRepository, ArticleConverter articleConverter) {
        this.tagRepository = tagRepository;
        this.articleConverter = articleConverter;
    }

    @Transactional(readOnly = true)
    public List<TagOption> findAllOptions() {
        return tagRepository.findAllByOrderByNameAsc()
                .stream()
                .map(articleConverter::toTagOption)
                .toList();
    }
}
