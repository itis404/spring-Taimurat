<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle}" /></title>
    <link rel="stylesheet" href="<c:url value='/css/main.css' />">
</head>
<body>
<jsp:include page="../fragments/header.jsp" />

<main class="container page">
    <section class="card page-intro">
        <p class="eyebrow">Author</p>
        <h1><c:out value="${author.profile.displayName}" /></h1>
        <p class="lead small-lead">@<c:out value="${author.username}" /></p>
        <c:if test="${not empty author.profile.bio}">
            <p><c:out value="${author.profile.bio}" /></p>
        </c:if>
        <c:if test="${not empty author.profile.favoriteSetup}">
            <p class="muted">Сетап: <c:out value="${author.profile.favoriteSetup}" /></p>
        </c:if>
    </section>

    <section class="section-head">
        <div>
            <p class="eyebrow">Publications</p>
            <h2>Опубликованные статьи</h2>
        </div>
    </section>

    <section class="grid two-columns">
        <c:forEach var="article" items="${articles}">
            <article class="card article-card wide">
                <div class="article-meta">
                    <a href="<c:url value='/categories/${article.categorySlug}' />"><c:out value="${article.categoryName}" /></a>
                    <span><c:out value="${article.viewsCount}" /> просмотров</span>
                </div>
                <h2><a href="<c:url value='/articles/${article.slug}' />"><c:out value="${article.title}" /></a></h2>
                <p><c:out value="${article.summary}" /></p>
            </article>
        </c:forEach>
        <c:if test="${empty articles}">
            <article class="card">
                <h2>Публикаций пока нет</h2>
                <p>У автора пока нет опубликованных статей</p>
            </article>
        </c:if>
    </section>
</main>

<jsp:include page="../fragments/footer.jsp" />
</body>
</html>
