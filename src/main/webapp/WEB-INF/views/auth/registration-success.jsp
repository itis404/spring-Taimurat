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
    <section class="card">
        <p class="eyebrow">Email confirmation</p>
        <h1>Проверьте email</h1>
        <p>
            Аккаунт для <b><c:out value="${email}" /></b> создан, но вход будет доступен только после подтверждения email.
        </p>

        <c:choose>
            <c:when test="${not empty verificationLink}">
                <div class="demo-box">
                    <h2>Dev-режим</h2>
                    <p>Пока SMTP не подключён, ссылка подтверждения:</p>
                    <p><a href="${verificationLink}"><c:out value="${verificationLink}" /></a></p>
                </div>
            </c:when>
        </c:choose>
    </section>
</main>

<jsp:include page="../fragments/footer.jsp" />
</body>
</html>
