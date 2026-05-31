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
    <section class="section-head top-section">
        <div>
            <p class="eyebrow">Admin</p>
            <h1>Модерация статей</h1>
            <p class="lead small-lead">Публикация, отклонение и архивация материалов.</p>
        </div>
        <a class="button secondary" href="<c:url value='/admin' />">В админку</a>
    </section>

    <c:if test="${not empty successMessage}">
        <div class="alert success"><c:out value="${successMessage}" /></div>
    </c:if>

    <section class="table-card card">
        <table class="data-table">
            <thead>
            <tr>
                <th>Статья</th>
                <th>Автор</th>
                <th>Категория</th>
                <th>Статус</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="article" items="${articles}">
                <tr>
                    <td>
                        <strong><c:out value="${article.title}" /></strong>
                        <p class="muted table-note"><c:out value="${article.summary}" /></p>
                    </td>
                    <td><c:out value="${article.authorDisplayName}" /></td>
                    <td><c:out value="${article.categoryName}" /></td>
                    <td><span class="badge neutral"><c:out value="${article.statusDisplayName}" /></span></td>
                    <td class="actions-cell">
                        <form method="post" action="<c:url value='/admin/articles/${article.id}/publish' />">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                            <button class="button primary small" type="submit">Опубликовать</button>
                        </form>
                        <form method="post" action="<c:url value='/admin/articles/${article.id}/reject' />">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                            <button class="button secondary small" type="submit">Отклонить</button>
                        </form>
                        <form method="post" action="<c:url value='/admin/articles/${article.id}/archive' />">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                            <button class="button danger small" type="submit">Архив</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty articles}">
                <tr>
                    <td colspan="5">Статей пока нет.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </section>
</main>

<jsp:include page="../fragments/footer.jsp" />
</body>
</html>
