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

<main class="container page">
    <section class="grid two-columns">
        <article class="card">
            <p class="eyebrow">Profile</p>
            <h1><c:out value="${user.profile.displayName}" /></h1>
            <dl class="status-list">
                <dt>Username</dt>
                <dd><c:out value="${user.username}" /></dd>
                <dt>Email</dt>
                <dd><c:out value="${user.email}" /></dd>
                <dt>Email verified</dt>
                <dd><span class="badge success"><c:out value="${user.emailVerified}" /></span></dd>
                <dt>Enabled</dt>
                <dd><span class="badge success"><c:out value="${user.enabled}" /></span></dd>
                <dt>Roles</dt>
                <dd>
                    <c:forEach var="role" items="${user.roles}">
                        <span class="badge"><c:out value="${role.name}" /></span>
                    </c:forEach>
                </dd>
            </dl>
        </article>

        <article class="card">
            <p class="eyebrow">Edit profile</p>
            <h2>Настройки профиля</h2>

            <c:if test="${profileUpdated}">
                <div class="alert success">Профиль обновлён.</div>
            </c:if>

            <c:url var="profileUrl" value="/profile" />
            <form:form cssClass="form" method="post" action="${profileUrl}" modelAttribute="profileUpdateForm">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <label class="field">
                    <span>Отображаемое имя</span>
                    <form:input path="displayName" cssClass="input" />
                    <form:errors path="displayName" cssClass="field-error" />
                </label>

                <label class="field">
                    <span>Опыт в FPV</span>
                    <form:input path="fpvExperienceLevel" cssClass="input" placeholder="например: beginner / intermediate" />
                    <form:errors path="fpvExperienceLevel" cssClass="field-error" />
                </label>

                <label class="field">
                    <span>Любимый сетап</span>
                    <form:textarea path="favoriteSetup" cssClass="textarea" rows="3" />
                    <form:errors path="favoriteSetup" cssClass="field-error" />
                </label>

                <label class="field">
                    <span>О себе</span>
                    <form:textarea path="bio" cssClass="textarea" rows="5" />
                    <form:errors path="bio" cssClass="field-error" />
                </label>

                <button class="button primary" type="submit">Сохранить</button>
            </form:form>
        </article>
    </section>
</main>

<jsp:include page="../fragments/footer.jsp" />
</body>
</html>
