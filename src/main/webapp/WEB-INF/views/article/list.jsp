<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle}" /></title>
    <link rel="stylesheet" href="<c:url value='/css/main.css' />">
    <script defer src="<c:url value='/js/article-search.js' />"></script>
</head>
<body>
<jsp:include page="../fragments/header.jsp" />

<main class="container page">
    <section class="section-head top-section">
        <div>
            <p class="eyebrow">Knowledge base</p>
            <h1>Статьи</h1>
            <p class="lead small-lead">Публичные гайды и заметки по сборке, настройке и пилотированию FPV-квадрокоптеров.</p>
        </div>
        <sec:authorize access="hasAnyRole('WRITER', 'ADMIN')">
            <a class="button primary" href="<c:url value='/articles/new' />">Написать статью</a>
        </sec:authorize>
    </section>

    <form id="article-filter-form" class="filter-panel" method="get" action="<c:url value='/articles' />" data-ajax-url="<c:url value='/ajax/articles/search' />">
        <div class="filter-grid">
            <label class="field">
                Поиск
                <input class="input" type="search" name="q" value="<c:out value='${searchForm.q}' />" placeholder="Название, краткое описание или текст статьи">
            </label>

            <label class="field">
                Категория
                <select class="input" name="category">
                    <option value="">Все категории</option>
                    <c:forEach var="category" items="${categories}">
                        <option value="${category.slug}" ${searchForm.category == category.slug ? 'selected' : ''}>
                            <c:out value="${category.name}" />
                        </option>
                    </c:forEach>
                </select>
            </label>

            <label class="field">
                Тег
                <select class="input" name="tag">
                    <option value="">Все теги</option>
                    <c:forEach var="tag" items="${tags}">
                        <option value="${tag.slug}" ${searchForm.tag == tag.slug ? 'selected' : ''}>
                            <c:out value="${tag.name}" />
                        </option>
                    </c:forEach>
                </select>
            </label>

            <label class="field">
                Сортировка
                <select class="input" name="sort">
                    <option value="newest" ${searchForm.sort == 'newest' || empty searchForm.sort ? 'selected' : ''}>Сначала новые</option>
                    <option value="views" ${searchForm.sort == 'views' ? 'selected' : ''}>По просмотрам</option>
                    <option value="title" ${searchForm.sort == 'title' ? 'selected' : ''}>По названию</option>
                </select>
            </label>
        </div>

        <div class="filter-actions">
            <label class="checkbox-pill">
                <input type="checkbox" name="videoOnly" value="true" ${searchForm.videoOnly ? 'checked' : ''}>
                Только с видео
            </label>
            <button class="button secondary" type="submit">Применить фильтры</button>
            <a class="button secondary" href="<c:url value='/articles' />">Сбросить</a>
            <span id="article-search-status" class="muted search-status">Найдено: <c:out value="${fn:length(articles)}" /></span>
        </div>
    </form>

    <section class="grid two-columns article-layout">
        <div id="article-list" class="article-list">
            <c:forEach var="article" items="${articles}">
                <article class="card article-card wide">
                    <div class="article-meta">
                        <a href="<c:url value='/categories/${article.categorySlug}' />"><c:out value="${article.categoryName}" /></a>
                        <span><c:out value="${article.viewsCount}" /> просмотров</span>
                    </div>
                    <h2><a href="<c:url value='/articles/${article.slug}' />"><c:out value="${article.title}" /></a></h2>
                    <p><c:out value="${article.summary}" /></p>
                    <div class="tag-row">
                        <c:forEach var="tag" items="${article.tags}">
                            <span class="tag"><c:out value="${tag.name}" /></span>
                        </c:forEach>
                    </div>
                    <div class="article-footer">
                        Автор: <a href="<c:url value='/authors/${article.authorUsername}' />"><c:out value="${article.authorDisplayName}" /></a>
                    </div>
                </article>
            </c:forEach>

            <c:if test="${empty articles}">
                <div class="card empty-search-card">
                    <h2>Ничего не найдено</h2>
                    <p>Попробуйте изменить поисковый запрос, категорию, тег или сортировку.</p>
                </div>
            </c:if>
        </div>

        <aside class="sidebar-stack">
            <section class="card sidebar-card">
                <h2>Категории</h2>
                <div class="category-links">
                    <c:forEach var="category" items="${categories}">
                        <a href="<c:url value='/categories/${category.slug}' />"><c:out value="${category.name}" /></a>
                    </c:forEach>
                </div>
            </section>

            <section class="card sidebar-card">
                <h2>Популярное</h2>
                <div class="compact-list">
                    <c:forEach var="article" items="${articleStatistics.mostViewed}">
                        <a href="<c:url value='/articles/${article.slug}' />">
                            <span><c:out value="${article.title}" /></span>
                            <small><c:out value="${article.viewsCount}" /> просмотров</small>
                        </a>
                    </c:forEach>
                    <c:if test="${empty articleStatistics.mostViewed}">
                        <p class="muted">Пока нет опубликованных статей.</p>
                    </c:if>
                </div>
            </section>

            <section class="card sidebar-card">
                <h2>Обсуждаемое</h2>
                <div class="compact-list">
                    <c:forEach var="article" items="${articleStatistics.discussed}">
                        <a href="<c:url value='/articles/${article.slug}' />"><c:out value="${article.title}" /></a>
                    </c:forEach>
                    <c:if test="${empty articleStatistics.discussed}">
                        <p class="muted">Пока нет статей с комментариями.</p>
                    </c:if>
                </div>
            </section>
        </aside>
    </section>
</main>

<jsp:include page="../fragments/footer.jsp" />
</body>
</html>
