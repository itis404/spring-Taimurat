<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
    <section class="hero">
        <div class="hero-content">
            <p class="eyebrow">FPV education forum</p>
            <h1>База знаний и форум по FPV-квадрокоптерам</h1>
            <p class="lead">
                Статьи, гайды, разборы сборок, настройка Betaflight, ELRS, видеосвязь,
                аккумуляторы, симуляторы и пилотирование
            </p>
            <div class="hero-actions">
                <a class="button primary" href="<c:url value='/articles' />">Смотреть статьи</a>
                <sec:authorize access="hasAnyRole('WRITER', 'ADMIN')">
                    <a class="button secondary" href="<c:url value='/articles/new' />">Написать статью</a>
                </sec:authorize>
                <a class="button secondary" href="<c:url value='/system/status' />">Проверить инфраструктуру</a>
            </div>
        </div>
    </section>

    <section class="section-head">
        <div>
            <p class="eyebrow">Latest</p>
            <h2>Последние опубликованные статьи</h2>
        </div>
        <a class="text-link" href="<c:url value='/articles' />">Все статьи</a>
    </section>

    <section class="grid three-columns">
        <c:forEach var="article" items="${latestArticles}">
            <article class="card article-card">
                <div class="article-meta">
                    <a href="<c:url value='/categories/${article.categorySlug}' />"><c:out value="${article.categoryName}" /></a>
                    <span><c:out value="${article.viewsCount}" /> просмотров</span>
                </div>
                <h2><a href="<c:url value='/articles/${article.slug}' />"><c:out value="${article.title}" /></a></h2>
                <p><c:out value="${article.summary}" /></p>
                <div class="article-footer">
                    <a href="<c:url value='/authors/${article.authorUsername}' />"><c:out value="${article.authorDisplayName}" /></a>
                </div>
            </article>
        </c:forEach>
        <c:if test="${empty latestArticles}">
            <article class="card">
                <h2>Статей пока нет</h2>
                <p>Зайдите под writer/admin и создайте первую публикацию</p>
            </article>
        </c:if>
    </section>

    <section class="section-head">
        <div>
            <p class="eyebrow">Topics</p>
            <h2>Категории</h2>
        </div>
    </section>

    <section class="grid three-columns">
        <c:forEach var="category" items="${categories}">
            <article class="card">
                <h2><a href="<c:url value='/categories/${category.slug}' />"><c:out value="${category.name}" /></a></h2>
                <p><c:out value="${category.description}" /></p>
            </article>
        </c:forEach>
    </section>
</main>

<jsp:include page="../fragments/footer.jsp" />
</body>
</html>
