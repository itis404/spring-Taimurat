package ru.itis.fpvhub.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.fpvhub.cache.CacheInvalidationService;
import ru.itis.fpvhub.converter.CommentConverter;
import ru.itis.fpvhub.dto.CommentView;
import ru.itis.fpvhub.entity.ArticleEntity;
import ru.itis.fpvhub.entity.CommentEntity;
import ru.itis.fpvhub.entity.UserEntity;
import ru.itis.fpvhub.exception.ResourceNotFoundException;
import ru.itis.fpvhub.form.CommentForm;
import ru.itis.fpvhub.repository.ArticleRepository;
import ru.itis.fpvhub.repository.CommentRepository;
import ru.itis.fpvhub.repository.UserRepository;
import ru.itis.fpvhub.security.CustomUserPrincipal;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CommentConverter commentConverter;
    private final CacheInvalidationService cacheInvalidationService;

    public CommentService(
            CommentRepository commentRepository,
            ArticleRepository articleRepository,
            UserRepository userRepository,
            CommentConverter commentConverter,
            CacheInvalidationService cacheInvalidationService
    ) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.commentConverter = commentConverter;
        this.cacheInvalidationService = cacheInvalidationService;
    }

    @Transactional(readOnly = true)
    public List<CommentView> findByArticle(Long articleId, Authentication authentication) {
        return commentRepository.findDetailedByArticleId(articleId)
                .stream()
                .map(comment -> commentConverter.toView(comment, canDelete(comment, authentication)))
                .toList();
    }

    @Transactional
    public String create(Long articleId, CommentForm form, Authentication authentication) {
        ArticleEntity article = articleRepository.findDetailedById(articleId)
                .filter(ArticleEntity::isPublished)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        UserEntity author = getCurrentUser(authentication);
        CommentEntity comment = new CommentEntity(form.getContent().trim(), article, author);
        commentRepository.save(comment);
        cacheInvalidationService.evictPublicContentCaches();
        return article.getSlug();
    }

    @Transactional
    public String delete(Long commentId, Authentication authentication) {
        CommentEntity comment = commentRepository.findDetailedById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        assertCanDelete(comment, authentication);
        comment.softDelete();
        cacheInvalidationService.evictPublicContentCaches();
        return comment.getArticle().getSlug();
    }

    private UserEntity getCurrentUser(Authentication authentication) {
        Long userId = extractUserId(authentication);
        return userRepository.findDetailedById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }

    private boolean canDelete(CommentEntity comment, Authentication authentication) {
        if (comment.isDeleted() || !hasCustomPrincipal(authentication)) {
            return false;
        }
        Long currentUserId = extractUserId(authentication);
        return isAdmin(authentication)
                || comment.getAuthor().getId().equals(currentUserId)
                || comment.getArticle().getAuthor().getId().equals(currentUserId);
    }

    private void assertCanDelete(CommentEntity comment, Authentication authentication) {
        if (!canDelete(comment, authentication)) {
            throw new ResourceNotFoundException("Comment not found");
        }
    }

    private boolean hasCustomPrincipal(Authentication authentication) {
        return authentication != null && authentication.getPrincipal() instanceof CustomUserPrincipal;
    }

    private Long extractUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserPrincipal customUserPrincipal) {
            return customUserPrincipal.getId();
        }
        throw new IllegalStateException("Unsupported principal type");
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
    }
}
