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
            <p class="eyebrow">Topics</p>
            <h1>Категории</h1>
            <p class="lead small-lead">Разделы базы знаний FPVHub</p>
        </div>
    </section>

    <section class="grid three-columns">
        <c:forEach var="category" items="${categories}">
            <article class="card">
                <h2><a href="<c:url value='/categories/${category.slug}' />"><c:out value="${category.name}" /></a></h2>
                <p><c:out value="${category.description}" /></p>
            </article>
        </c:forEach>
    </section>
</main>

<jsp:include page="../fragments/footer.jsp" />
</body>
</html>
