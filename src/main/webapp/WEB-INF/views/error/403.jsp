<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>403 — доступ запрещён</title>
    <link rel="stylesheet" href="<c:url value='/css/main.css' />">
</head>
<body>
<jsp:include page="../fragments/header.jsp" />
<main class="container page narrow">
    <section class="card">
        <p class="eyebrow">403</p>
        <h1>Доступ запрещён</h1>
        <p>У вас нет прав для доступа к этому ресурсу</p>
        <a class="button primary" href="<c:url value='/' />">На главную</a>
    </section>
</main>
<jsp:include page="../fragments/footer.jsp" />
</body>
</html>
