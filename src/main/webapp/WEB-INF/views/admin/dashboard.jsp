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
    <section class="hero compact">
        <p class="eyebrow">Admin</p>
        <h1>Админ-панель</h1>
        <p class="lead">Служебная зона проекта: модерация, статистика, контроль Redis-кэша и проверка интеграций.</p>
    </section>

    <c:if test="${not empty successMessage}">
        <div class="alert success"><c:out value="${successMessage}" /></div>
    </c:if>

    <section class="grid three-columns">
        <article class="card">
            <h2>Пользователи</h2>
            <p class="metric"><c:out value="${userCount}" /></p>
            <p>Локальные пользователи, включая аккаунты, созданные через GitHub OAuth.</p>
        </article>
        <article class="card">
            <h2>Опубликовано</h2>
            <p class="metric"><c:out value="${publishedArticleCount}" /></p>
            <p>Публичные статьи, видимые гостям и пользователям.</p>
        </article>
        <article class="card">
            <h2>На модерации</h2>
            <p class="metric"><c:out value="${pendingArticleCount}" /></p>
            <p>Статьи со статусом PENDING_REVIEW.</p>
        </article>
        <article class="card">
            <h2>Комментарии</h2>
            <p class="metric"><c:out value="${commentCount}" /></p>
            <p>Неудалённые пользовательские комментарии.</p>
        </article>
        <article class="card">
            <h2>OAuth-связки</h2>
            <p class="metric"><c:out value="${oauthAccountCount}" /></p>
            <p>Аккаунты внешних провайдеров, связанные с локальными пользователями.</p>
        </article>
        <article class="card">
            <h2>Redis-кэш</h2>
            <p>Кэшируются последние статьи, категории и блок статистики на странице статей.</p>
            <form method="post" action="<c:url value='/admin/cache/evict' />">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <button class="button secondary" type="submit">Сбросить кэш</button>
            </form>
        </article>
    </section>

    <section class="card mt-lg">
        <h2>Модерация</h2>
        <p>Публикация, отклонение и архивирование статей авторов.</p>
        <a class="button" href="<c:url value='/admin/articles' />">Открыть статьи</a>
    </section>
</main>

<jsp:include page="../fragments/footer.jsp" />
</body>
</html>
