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

<main class="container page narrow">
    <section class="card auth-card">
        <p class="eyebrow">Authentication</p>
        <h1>Вход</h1>

        <c:if test="${param.error != null}">
            <div class="alert error">
                Неверный логин/пароль или email ещё не подтверждён.
            </div>
        </c:if>
        <c:if test="${param.logout != null}">
            <div class="alert success">Вы вышли из аккаунта.</div>
        </c:if>
        <c:if test="${param.verified != null}">
            <div class="alert success">Email подтверждён. Теперь можно войти.</div>
        </c:if>
        <c:if test="${not empty oauthError}">
            <div class="alert error"><c:out value="${oauthError}" /></div>
        </c:if>

        <a class="button oauth" href="<c:url value='/oauth2/github/start' />">Войти через GitHub</a>
        <div class="separator"><span>или через пароль</span></div>

        <form class="form" method="post" action="<c:url value='/login' />">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

            <label class="field">
                <span>Username или email</span>
                <input class="input" type="text" name="usernameOrEmail" autocomplete="username" required>
            </label>

            <label class="field">
                <span>Пароль</span>
                <input class="input" type="password" name="password" autocomplete="current-password" required>
            </label>

            <button class="button primary" type="submit">Войти</button>
        </form>

        <p class="muted auth-hint">
            Нет аккаунта? <a href="<c:url value='/registration' />">Зарегистрироваться</a>
        </p>

    </section>
</main>

<jsp:include page="../fragments/footer.jsp" />
</body>
</html>
