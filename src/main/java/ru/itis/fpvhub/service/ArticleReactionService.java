package ru.itis.fpvhub.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.fpvhub.dto.ReactionResponse;
import ru.itis.fpvhub.dto.ReactionSummaryView;
import ru.itis.fpvhub.entity.ArticleEntity;
import ru.itis.fpvhub.entity.ArticleReactionEntity;
import ru.itis.fpvhub.entity.UserEntity;
import ru.itis.fpvhub.entity.enums.ArticleReactionType;
import ru.itis.fpvhub.exception.ResourceNotFoundException;
import ru.itis.fpvhub.repository.ArticleReactionRepository;
import ru.itis.fpvhub.repository.ArticleRepository;
import ru.itis.fpvhub.repository.UserRepository;
import ru.itis.fpvhub.security.CustomUserPrincipal;

import java.util.Optional;

@Service
public class ArticleReactionService {

    private final ArticleReactionRepository articleReactionRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleReactionService(
            ArticleReactionRepository articleReactionRepository,
            ArticleRepository articleRepository,
            UserRepository userRepository
    ) {
        this.articleReactionRepository = articleReactionRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public ReactionSummaryView getSummary(Long articleId, Authentication authentication) {
        ArticleReactionType currentReaction = null;
        if (hasCustomPrincipal(authentication)) {
            Long userId = extractUserId(authentication);
            currentReaction = articleReactionRepository.findByArticleIdAndUserId(articleId, userId)
                    .map(ArticleReactionEntity::getType)
                    .orElse(null);
        }
        return buildSummary(articleId, currentReaction);
    }

    @Transactional
    public ReactionResponse react(Long articleId, ArticleReactionType type, Authentication authentication) {
        ArticleEntity article = articleRepository.findDetailedById(articleId)
                .filter(ArticleEntity::isPublished)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        UserEntity user = getCurrentUser(authentication);

        Optional<ArticleReactionEntity> existingReaction = articleReactionRepository.findDetailedByArticleIdAndUserId(articleId, user.getId());
        ArticleReactionType currentReaction;

        if (existingReaction.isPresent()) {
            ArticleReactionEntity reaction = existingReaction.get();
            if (reaction.getType() == type) {
                articleReactionRepository.delete(reaction);
                currentReaction = null;
            } else {
                reaction.setType(type);
                currentReaction = type;
            }
        } else {
            articleReactionRepository.save(new ArticleReactionEntity(article, user, type));
            currentReaction = type;
        }

        ReactionSummaryView summary = buildSummary(articleId, currentReaction);
        return new ReactionResponse(summary.getLikes(), summary.getDislikes(), summary.getCurrentUserReaction());
    }

    private ReactionSummaryView buildSummary(Long articleId, ArticleReactionType currentReaction) {
        long likes = articleReactionRepository.countByArticleIdAndType(articleId, ArticleReactionType.LIKE);
        long dislikes = articleReactionRepository.countByArticleIdAndType(articleId, ArticleReactionType.DISLIKE);
        return new ReactionSummaryView(likes, dislikes, currentReaction);
    }

    private boolean hasCustomPrincipal(Authentication authentication) {
        return authentication != null && authentication.getPrincipal() instanceof CustomUserPrincipal;
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
}
