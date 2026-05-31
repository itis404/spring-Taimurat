package ru.itis.fpvhub.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.fpvhub.dto.api.ApiErrorResponse;
import ru.itis.fpvhub.dto.api.ArticleApiListResponse;
import ru.itis.fpvhub.dto.api.ArticleApiRequest;
import ru.itis.fpvhub.dto.api.ArticleApiResponse;
import ru.itis.fpvhub.service.ArticleService;

@RestController
@RequestMapping("/api/v1/articles")
@Tag(name = "Articles", description = "REST API for FPVHub articles")
public class ArticleRestController {

    private final ArticleService articleService;

    public ArticleRestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    @Operation(summary = "Get published articles", description = "Returns all published articles visible to anonymous users")
    @ApiResponse(responseCode = "200", description = "Published articles returned")
    public ArticleApiListResponse getPublishedArticles() {
        return articleService.findPublishedArticlesForApi();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get article by id",
            description = "Returns a published article. Draft and moderation articles are visible only to their author or admin."
    )
    @ApiResponse(responseCode = "200", description = "Article returned")
    @ApiResponse(
            responseCode = "404",
            description = "Article not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    public ArticleApiResponse getById(
            @Parameter(description = "Article id") @PathVariable("id") Long id,
            Authentication authentication
    ) {
        return articleService.findArticleForApi(id, authentication);
    }

    @PostMapping
    @SecurityRequirement(name = "basicAuth")
    @Operation(summary = "Create article", description = "Creates an article. Requires WRITER or ADMIN role.")
    @ApiResponse(responseCode = "201", description = "Article created")
    @ApiResponse(
            responseCode = "400",
            description = "Validation error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    public ResponseEntity<ArticleApiResponse> create(
            @Valid @RequestBody ArticleApiRequest request,
            Authentication authentication
    ) {
        ArticleApiResponse response = articleService.createFromApi(authentication, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "basicAuth")
    @Operation(summary = "Update article", description = "Updates an article. Requires article author or ADMIN role.")
    @ApiResponse(responseCode = "200", description = "Article updated")
    @ApiResponse(
            responseCode = "400",
            description = "Validation error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Article not found or access denied",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    public ArticleApiResponse update(
            @Parameter(description = "Article id") @PathVariable("id") Long id,
            @Valid @RequestBody ArticleApiRequest request,
            Authentication authentication
    ) {
        return articleService.updateFromApi(id, authentication, request);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "basicAuth")
    @Operation(summary = "Archive article", description = "Archives an article. Requires article author or ADMIN role.")
    @ApiResponse(responseCode = "204", description = "Article archived")
    @ApiResponse(
            responseCode = "404",
            description = "Article not found or access denied",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    public ResponseEntity<Void> delete(
            @Parameter(description = "Article id") @PathVariable("id") Long id,
            Authentication authentication
    ) {
        articleService.archiveFromApi(id, authentication);
        return ResponseEntity.noContent().build();
    }
}
