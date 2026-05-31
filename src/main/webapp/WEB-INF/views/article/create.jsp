<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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

<main class="container page narrow">
    <section class="card">
        <p class="eyebrow">Writer tools</p>
        <h1>Новая статья</h1>
        <p class="muted">Создайте черновик или отправьте статью на модерацию. Публичной она станет после ревью администратором</p>

        <form:form cssClass="form" method="post" action="${pageContext.request.contextPath}/articles" modelAttribute="articleForm">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

            <label class="field">
                <span>Название</span>
                <form:input cssClass="input" path="title" />
                <form:errors path="title" cssClass="field-error" />
            </label>

            <label class="field">
                <span>Краткое описание</span>
                <form:textarea cssClass="textarea" path="summary" rows="3" />
                <form:errors path="summary" cssClass="field-error" />
            </label>

            <label class="field">
                <span>Категория</span>
                <form:select cssClass="input" path="categoryId">
                    <form:option value="" label="Выберите категорию" />
                    <c:forEach var="category" items="${categories}">
                        <form:option value="${category.id}" label="${category.name}" />
                    </c:forEach>
                </form:select>
                <form:errors path="categoryId" cssClass="field-error" />
            </label>

            <div class="field">
                <span>Теги</span>
                <div class="checkbox-grid">
                    <c:forEach var="tag" items="${tags}">
                        <label class="checkbox-pill">
                            <form:checkbox path="tagIds" value="${tag.id}" />
                            <span><c:out value="${tag.name}" /></span>
                        </label>
                    </c:forEach>
                </div>
            </div>

            <label class="field">
                <span>Ссылка на обложку</span>
                <form:input cssClass="input" path="coverImageUrl" placeholder="https://..." />
                <form:errors path="coverImageUrl" cssClass="field-error" />
            </label>

            <label class="field">
                <span>Ссылка на видео</span>
                <form:input cssClass="input" path="videoUrl" placeholder="https://youtube.com/..." />
                <form:errors path="videoUrl" cssClass="field-error" />
            </label>

            <label class="field">
                <span>Текст статьи</span>
                <form:textarea cssClass="textarea article-editor" path="content" rows="14" />
                <form:errors path="content" cssClass="field-error" />
            </label>

            <div class="hero-actions">
                <button class="button secondary" name="publicationAction" value="draft" type="submit">Сохранить черновик</button>
                <button class="button primary" name="publicationAction" value="review" type="submit">Отправить на модерацию</button>
            </div>
        </form:form>
    </section>
</main>

<jsp:include page="../fragments/footer.jsp" />
</body>
</html>
