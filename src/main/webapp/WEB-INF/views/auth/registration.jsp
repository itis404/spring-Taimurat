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
    <section class="card auth-card">
        <p class="eyebrow">Registration</p>
        <h1>Создать аккаунт</h1>
        <p class="muted">После регистрации нужно подтвердить email</p>

        <c:url var="registrationUrl" value="/registration" />
        <form:form cssClass="form" method="post" action="${registrationUrl}" modelAttribute="registrationForm">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <label class="field">
                <span>Username</span>
                <form:input path="username" cssClass="input" autocomplete="username" />
                <form:errors path="username" cssClass="field-error" />
            </label>

            <label class="field">
                <span>Email</span>
                <form:input path="email" cssClass="input" autocomplete="email" />
                <form:errors path="email" cssClass="field-error" />
            </label>

            <label class="field">
                <span>Отображаемое имя</span>
                <form:input path="displayName" cssClass="input" />
                <form:errors path="displayName" cssClass="field-error" />
            </label>

            <label class="field">
                <span>Пароль</span>
                <form:password path="password" cssClass="input" autocomplete="new-password" />
                <form:errors path="password" cssClass="field-error" />
            </label>

            <label class="field">
                <span>Повтор пароля</span>
                <form:password path="passwordRepeat" cssClass="input" autocomplete="new-password" />
                <form:errors path="passwordRepeat" cssClass="field-error" />
            </label>

            <button class="button primary" type="submit">Зарегистрироваться</button>
        </form:form>

        <p class="muted auth-hint">
            Уже есть аккаунт? <a href="<c:url value='/login' />">Войти</a>
        </p>
    </section>
</main>

<jsp:include page="../fragments/footer.jsp" />
</body>
</html>
