package ru.itis.fpvhub.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.fpvhub.cache.CacheInvalidationService;
import ru.itis.fpvhub.converter.ArticleApiConverter;
import ru.itis.fpvhub.converter.ArticleConverter;
import ru.itis.fpvhub.dao.ArticleSearchDao;
import ru.itis.fpvhub.dto.ArticleCardView;
import ru.itis.fpvhub.dto.ArticleDetailsView;
import ru.itis.fpvhub.dto.ArticleStatisticsView;
import ru.itis.fpvhub.dto.api.ArticleApiListResponse;
import ru.itis.fpvhub.dto.api.ArticleApiRequest;
import ru.itis.fpvhub.dto.api.ArticleApiResponse;
import ru.itis.fpvhub.dto.CategoryOption;
import ru.itis.fpvhub.dto.TagOption;
import ru.itis.fpvhub.entity.ArticleEntity;
import ru.itis.fpvhub.entity.CategoryEntity;
import ru.itis.fpvhub.entity.TagEntity;
import ru.itis.fpvhub.entity.UserEntity;
import ru.itis.fpvhub.entity.enums.ArticleStatus;
import ru.itis.fpvhub.exception.ResourceNotFoundException;
import ru.itis.fpvhub.form.ArticleForm;
import ru.itis.fpvhub.form.ArticleSearchForm;
import ru.itis.fpvhub.repository.ArticleRepository;
import ru.itis.fpvhub.repository.CategoryRepository;
import ru.itis.fpvhub.repository.TagRepository;
import ru.itis.fpvhub.repository.UserRepository;
import ru.itis.fpvhub.security.CustomUserPrincipal;
import ru.itis.fpvhub.util.SlugGenerator;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class ArticleService {

    private static final String ACTION_DRAFT = "draft";

    private final ArticleRepository articleRepository;
    private final ArticleSearchDao articleSearchDao;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ArticleConverter articleConverter;
    private final ArticleApiConverter articleApiConverter;
    private final CacheInvalidationService cacheInvalidationService;

    public ArticleService(
            ArticleRepository articleRepository,
            ArticleSearchDao articleSearchDao,
            CategoryRepository categoryRepository,
            TagRepository tagRepository,
            UserRepository userRepository,
            ArticleConverter articleConverter,
            ArticleApiConverter articleApiConverter,
            CacheInvalidationService cacheInvalidationService
    ) {
        this.articleRepository = articleRepository;
        this.articleSearchDao = articleSearchDao;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.articleConverter = articleConverter;
        this.articleApiConverter = articleApiConverter;
        this.cacheInvalidationService = cacheInvalidationService;
    }

    @Transactional(readOnly = true)
    public List<ArticleCardView> findPublishedArticles(String query) {
        ArticleSearchForm form = new ArticleSearchForm();
        form.setQ(query);
        return searchPublishedArticles(form);
    }

    @Transactional(readOnly = true)
    public List<ArticleCardView> searchPublishedArticles(ArticleSearchForm form) {
        return articleSearchDao.searchPublished(normalizeSearchForm(form))
                .stream()
                .map(articleConverter::toCard)
                .toList();
    }

    @Transactional(readOnly = true)
    public ArticleStatisticsView getArticleStatistics() {
        List<ArticleCardView> mostViewed = articleRepository.findMostViewedPublished(PageRequest.of(0, 5))
                .stream()
                .map(articleConverter::toCard)
                .toList();

        List<ArticleCardView> discussed = articleRepository.findDiscussedPublishedArticles(1)
                .stream()
                .limit(5)
                .map(articleConverter::toCard)
                .toList();

        return new ArticleStatisticsView(mostViewed, discussed);
    }

    @Transactional(readOnly = true)
    public List<ArticleCardView> findLatestPublishedArticles() {
        return articleRepository.findTop6ByStatusOrderByPublishedAtDesc(ArticleStatus.PUBLISHED)
                .stream()
                .map(articleConverter::toCard)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ArticleCardView> findPublishedByCategory(String categorySlug) {
        return articleRepository.findPublishedArticlesByCategorySlug(categorySlug)
                .stream()
                .map(articleConverter::toCard)
                .toList();
    }

    @Transactional
    public ArticleDetailsView getPublishedDetails(String slug, Authentication authentication) {
        ArticleEntity article = articleRepository.findPublishedDetailedBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        article.incrementViews();
        boolean canEdit = canEditArticle(article, authentication);
        return articleConverter.toDetails(article, canEdit);
    }


    @Transactional(readOnly = true)
    public ArticleApiListResponse findPublishedArticlesForApi() {
        List<ArticleApiResponse> articles = articleRepository.findPublishedArticles()
                .stream()
                .map(articleApiConverter::toResponse)
                .toList();
        return new ArticleApiListResponse(articles);
    }

    @Transactional(readOnly = true)
    public ArticleApiResponse findArticleForApi(Long articleId, Authentication authentication) {
        ArticleEntity article = getDetailedArticle(articleId);
        if (!article.isPublished() && !canEditArticle(article, authentication)) {
            throw new ResourceNotFoundException("Article not found");
        }
        return articleApiConverter.toResponse(article);
    }

    @Transactional
    public ArticleApiResponse createFromApi(Authentication authentication, ArticleApiRequest request) {
        ArticleEntity article = create(authentication, articleApiConverter.toForm(request));
        return articleApiConverter.toResponse(article);
    }

    @Transactional
    public ArticleApiResponse updateFromApi(Long articleId, Authentication authentication, ArticleApiRequest request) {
        ArticleEntity article = update(articleId, authentication, articleApiConverter.toForm(request));
        return articleApiConverter.toResponse(article);
    }

    @Transactional
    public void archiveFromApi(Long articleId, Authentication authentication) {
        archive(articleId, authentication);
    }

    @Transactional(readOnly = true)
    public ArticleForm createEmptyForm() {
        return new ArticleForm();
    }

    @Transactional
    public ArticleEntity create(Authentication authentication, ArticleForm form) {
        UserEntity author = getCurrentUser(authentication);
        CategoryEntity category = getCategory(form.getCategoryId());
        Set<TagEntity> tags = getTags(form.getTagIds());

        ArticleStatus status = ACTION_DRAFT.equals(form.getPublicationAction())
                ? ArticleStatus.DRAFT
                : ArticleStatus.PENDING_REVIEW;

        ArticleEntity article = new ArticleEntity(
                form.getTitle().trim(),
                generateUniqueSlug(form.getTitle()),
                form.getSummary().trim(),
                form.getContent().trim(),
                emptyToNull(form.getCoverImageUrl()),
                emptyToNull(form.getVideoUrl()),
                status,
                author,
                category,
                tags
        );

        ArticleEntity savedArticle = articleRepository.save(article);
        cacheInvalidationService.evictPublicContentCaches();
        return savedArticle;
    }

    @Transactional(readOnly = true)
    public ArticleForm getFormForEdit(Long articleId, Authentication authentication) {
        ArticleEntity article = getDetailedArticle(articleId);
        assertCanEdit(article, authentication);
        return articleConverter.toForm(article);
    }

    @Transactional
    public ArticleEntity update(Long articleId, Authentication authentication, ArticleForm form) {
        ArticleEntity article = getDetailedArticle(articleId);
        assertCanEdit(article, authentication);

        CategoryEntity category = getCategory(form.getCategoryId());
        Set<TagEntity> tags = getTags(form.getTagIds());
        articleConverter.updateEntity(article, form, category, tags);

        if (ACTION_DRAFT.equals(form.getPublicationAction())) {
            article.saveAsDraft();
        } else {
            article.submitForReview();
        }

        cacheInvalidationService.evictPublicContentCaches();
        return article;
    }

    @Transactional
    public void archive(Long articleId, Authentication authentication) {
        ArticleEntity article = getDetailedArticle(articleId);
        assertCanEdit(article, authentication);
        article.archive();
        cacheInvalidationService.evictPublicContentCaches();
    }

    @Transactional(readOnly = true)
    public List<ArticleCardView> findCurrentUserArticles(Authentication authentication) {
        Long userId = extractUserId(authentication);
        return articleRepository.findDetailedByAuthorId(userId)
                .stream()
                .map(articleConverter::toCard)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ArticleCardView> findPublishedByAuthor(String username) {
        return articleRepository.findPublishedByAuthorUsername(username)
                .stream()
                .map(articleConverter::toCard)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ArticleCardView> findAllForAdmin() {
        return articleRepository.findAllForAdmin()
                .stream()
                .map(articleConverter::toCard)
                .toList();
    }

    @Transactional
    public void publish(Long articleId) {
        getDetailedArticle(articleId).publish();
        cacheInvalidationService.evictPublicContentCaches();
    }

    @Transactional
    public void reject(Long articleId) {
        getDetailedArticle(articleId).reject();
        cacheInvalidationService.evictPublicContentCaches();
    }

    @Transactional
    public void archiveByAdmin(Long articleId) {
        getDetailedArticle(articleId).archive();
        cacheInvalidationService.evictPublicContentCaches();
    }

    @Transactional(readOnly = true)
    public List<CategoryOption> getCategoryOptions() {
        return categoryRepository.findAllByOrderByNameAsc()
                .stream()
                .map(articleConverter::toCategoryOption)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TagOption> getTagOptions() {
        return tagRepository.findAllByOrderByNameAsc()
                .stream()
                .map(articleConverter::toTagOption)
                .toList();
    }


    private ArticleSearchForm normalizeSearchForm(ArticleSearchForm form) {
        ArticleSearchForm normalized = form == null ? new ArticleSearchForm() : form;
        if (normalized.getSort() == null || normalized.getSort().isBlank()) {
            normalized.setSort("newest");
        }
        return normalized;
    }

    private ArticleEntity getDetailedArticle(Long articleId) {
        return articleRepository.findDetailedById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
    }

    private CategoryEntity getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    private Set<TagEntity> getTags(Set<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return new LinkedHashSet<>();
        }
        return new LinkedHashSet<>(tagRepository.findAllByIdIn(tagIds));
    }

    private UserEntity getCurrentUser(Authentication authentication) {
        Long userId = extractUserId(authentication);
        return userRepository.findDetailedById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }

    private Long extractUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserPrincipal customUserPrincipal) {
            return customUserPrincipal.getId();
        }
        throw new IllegalStateException("Unsupported principal type");
    }

    private boolean canEditArticle(ArticleEntity article, Authentication authentication) {
        if (!hasCustomPrincipal(authentication)) {
            return false;
        }
        return isAdmin(authentication) || article.getAuthor().getId().equals(extractUserId(authentication));
    }

    private boolean hasCustomPrincipal(Authentication authentication) {
        return authentication != null && authentication.getPrincipal() instanceof CustomUserPrincipal;
    }

    private void assertCanEdit(ArticleEntity article, Authentication authentication) {
        if (!canEditArticle(article, authentication)) {
            throw new ResourceNotFoundException("Article not found");
        }
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
    }

    private String generateUniqueSlug(String title) {
        String base = SlugGenerator.fromTitle(title);
        String candidate = base;
        int index = 2;
        while (articleRepository.existsBySlug(candidate)) {
            candidate = base + "-" + index;
            index++;
        }
        return candidate;
    }

    private String emptyToNull(String value) {
        if (value == null || value.trim().isBlank()) {
            return null;
        }
        return value.trim();
    }
}
