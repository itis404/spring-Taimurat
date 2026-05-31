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
        <p class="eyebrow">Email verification</p>
        <h1><c:out value="${verificationResult.title}" /></h1>
        <p><c:out value="${verificationResult.message}" /></p>
        <c:choose>
            <c:when test="${verificationResult.success}">
                <a class="button primary" href="<c:url value='/login?verified' />">Перейти ко входу</a>
            </c:when>
            <c:otherwise>
                <a class="button primary" href="<c:url value='/registration' />">К регистрации</a>
            </c:otherwise>
        </c:choose>
    </section>
</main>

<jsp:include page="../fragments/footer.jsp" />
</body>
</html>
