<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="_csrf" content="${_csrf.token}">
    <meta name="_csrf_header" content="${_csrf.headerName}">
    <title><c:out value="${article.title}" /> — FPVHub</title>
    <link rel="stylesheet" href="<c:url value='/css/main.css' />">
</head>
<body>
<jsp:include page="../fragments/header.jsp" />

<main class="container page narrow">
    <c:if test="${not empty successMessage}">
        <div class="alert success"><c:out value="${successMessage}" /></div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert error"><c:out value="${errorMessage}" /></div>
    </c:if>

    <article class="card article-details">
        <div class="article-meta">
            <a href="<c:url value='/categories/${article.categorySlug}' />"><c:out value="${article.categoryName}" /></a>
            <span><c:out value="${article.viewsCount}" /> просмотров</span>
        </div>
        <h1><c:out value="${article.title}" /></h1>
        <p class="lead small-lead"><c:out value="${article.summary}" /></p>

        <div class="article-footer details-author">
            Автор: <a href="<c:url value='/authors/${article.authorUsername}' />"><c:out value="${article.authorDisplayName}" /></a>
        </div>

        <c:if test="${not empty article.coverImageUrl}">
            <img class="article-cover" src="<c:out value='${article.coverImageUrl}' />" alt="Обложка статьи">
        </c:if>

        <c:if test="${not empty article.videoUrl}">
            <div class="video-box">
                <p>Видео к статье:</p>
                <a href="<c:out value='${article.videoUrl}' />" target="_blank" rel="noopener noreferrer"><c:out value="${article.videoUrl}" /></a>
            </div>
        </c:if>

        <div class="article-body"><c:out value="${article.content}" /></div>

        <div class="tag-row">
            <c:forEach var="tag" items="${article.tags}">
                <span class="tag"><c:out value="${tag.name}" /></span>
            </c:forEach>
        </div>

        <c:url value="/ajax/articles/${article.id}/reaction" var="reactionUrl" />
        <section class="reaction-panel" data-reaction-url="${reactionUrl}">
            <div class="reaction-title">Оценка статьи</div>
            <sec:authorize access="isAuthenticated()">
                <button class="reaction-button ${reactionSummary.likedByCurrentUser ? 'active' : ''}" type="button" data-reaction-type="LIKE">
                    👍 <span id="likes-count"><c:out value="${reactionSummary.likes}" /></span>
                </button>
                <button class="reaction-button ${reactionSummary.dislikedByCurrentUser ? 'active' : ''}" type="button" data-reaction-type="DISLIKE">
                    👎 <span id="dislikes-count"><c:out value="${reactionSummary.dislikes}" /></span>
                </button>
            </sec:authorize>
            <sec:authorize access="isAnonymous()">
                <span class="muted">Лайки: <c:out value="${reactionSummary.likes}" />, дизлайки: <c:out value="${reactionSummary.dislikes}" />.</span>
                <a class="text-link" href="<c:url value='/login' />">Войдите, чтобы оценить статью</a>
            </sec:authorize>
        </section>

        <c:if test="${article.canEdit}">
            <div class="hero-actions compact-actions">
                <a class="button secondary" href="<c:url value='/articles/${article.id}/edit' />">Редактировать</a>
            </div>
        </c:if>
    </article>

    <section id="comments" class="comments-section">
        <div class="section-head">
            <div>
                <p class="eyebrow">Discussion</p>
                <h2>Комментарии</h2>
            </div>
        </div>

        <sec:authorize access="isAuthenticated()">
            <form class="card form comment-form" method="post" action="<c:url value='/articles/${article.id}/comments' />">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <label class="field">
                    Добавить комментарий
                    <textarea class="textarea" name="content" rows="5" maxlength="2000" required placeholder="Напишите вопрос, уточнение или дополнение к статье"></textarea>
                </label>
                <button class="button primary" type="submit">Отправить</button>
            </form>
        </sec:authorize>

        <sec:authorize access="isAnonymous()">
            <div class="card">
                <p>Чтобы оставить комментарий, <a class="text-link" href="<c:url value='/login' />">войдите в аккаунт</a>.</p>
            </div>
        </sec:authorize>

        <div class="comment-list">
            <c:choose>
                <c:when test="${empty comments}">
                    <div class="card">
                        <p>Комментариев пока нет. Можно быть первым</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="comment" items="${comments}">
                        <article class="card comment-card ${comment.deleted ? 'deleted' : ''}" id="comment-${comment.id}" data-comment-id="${comment.id}">
                            <div class="comment-head">
                                <div>
                                    <a class="text-link" href="<c:url value='/authors/${comment.authorUsername}' />"><c:out value="${comment.authorDisplayName}" /></a>
                                    <div class="muted"><c:out value="${comment.createdAt}" /></div>
                                </div>
                                <c:if test="${comment.canDelete}">
                                    <c:url value="/ajax/comments/${comment.id}/delete" var="deleteCommentUrl" />
                                    <button class="button danger small ajax-delete-comment" type="button" data-delete-url="${deleteCommentUrl}">
                                        Удалить
                                    </button>
                                    <noscript>
                                        <form method="post" action="<c:url value='/comments/${comment.id}/delete' />">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                            <button class="button danger small" type="submit">Удалить</button>
                                        </form>
                                    </noscript>
                                </c:if>
                            </div>
                            <p class="comment-content"><c:out value="${comment.content}" /></p>
                        </article>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </section>
</main>

<jsp:include page="../fragments/footer.jsp" />
<script src="<c:url value='/js/reactions.js' />"></script>
<script src="<c:url value='/js/comments.js' />"></script>
</body>
</html>
