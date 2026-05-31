<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>500 — ошибка сервера</title>
    <link rel="stylesheet" href="<c:url value='/css/main.css' />">
</head>
<body>
<jsp:include page="../fragments/header.jsp" />
<main class="container page narrow">
    <section class="card">
        <p class="eyebrow">500</p>
        <h1>Внутренняя ошибка сервера</h1>
        <p>¯\_(ツ)_/¯</p>
        <a class="button primary" href="<c:url value='/' />">На главную</a>
    </section>
</main>
<jsp:include page="../fragments/footer.jsp" />
</body>
</html>
