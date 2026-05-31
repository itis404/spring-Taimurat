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
        <p class="eyebrow">Infrastructure check</p>
        <h1>Статус системы</h1>

        <dl class="status-list">
            <dt>Application</dt>
            <dd><c:out value="${status.applicationName}" /></dd>

            <dt>Profiles</dt>
            <dd><c:out value="${status.activeProfiles}" /></dd>

            <dt>PostgreSQL</dt>
            <dd>
                <c:choose>
                    <c:when test="${status.databaseAvailable}">
                        <span class="badge success">available</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge danger">unavailable</span>
                    </c:otherwise>
                </c:choose>
            </dd>

            <dt>PostgreSQL version</dt>
            <dd><c:out value="${status.databaseVersion}" /></dd>

            <dt>Redis</dt>
            <dd>
                <c:choose>
                    <c:when test="${status.redisAvailable}">
                        <span class="badge success">available</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge danger">unavailable</span>
                    </c:otherwise>
                </c:choose>
            </dd>

            <dt>Redis probe</dt>
            <dd><c:out value="${status.redisProbeValue}" /></dd>
        </dl>
    </section>
</main>

<jsp:include page="../fragments/footer.jsp" />
</body>
</html>
