<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<header class="site-header">
    <div class="container header-inner">
        <a class="brand" href="<c:url value='/' />">FPVHub</a>
        <nav class="main-nav" aria-label="Основная навигация">
            <a class="nav-link ${activePage == 'home' ? 'active' : ''}" href="<c:url value='/' />">Главная</a>
            <a class="nav-link ${activePage == 'articles' ? 'active' : ''}" href="<c:url value='/articles' />">Статьи</a>
            <a class="nav-link ${activePage == 'categories' ? 'active' : ''}" href="<c:url value='/categories' />">Категории</a>
            <a class="nav-link ${activePage == 'system' ? 'active' : ''}" href="<c:url value='/system/status' />">Статус</a>

            <sec:authorize access="hasAnyRole('WRITER', 'ADMIN')">
                <a class="nav-link ${activePage == 'myArticles' ? 'active' : ''}" href="<c:url value='/my/articles' />">Мои статьи</a>
            </sec:authorize>

            <sec:authorize access="hasRole('ADMIN')">
                <a class="nav-link ${activePage == 'admin' ? 'active' : ''}" href="<c:url value='/admin' />">Админка</a>
            </sec:authorize>

            <sec:authorize access="isAuthenticated()">
                <a class="nav-link ${activePage == 'profile' ? 'active' : ''}" href="<c:url value='/profile' />">
                    <sec:authentication property="principal.username" />
                </a>
                <form class="logout-form" method="post" action="<c:url value='/logout' />">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                    <button class="nav-button" type="submit">Выйти</button>
                </form>
            </sec:authorize>

            <sec:authorize access="isAnonymous()">
                <a class="nav-link ${activePage == 'login' ? 'active' : ''}" href="<c:url value='/login' />">Войти</a>
                <a class="nav-link ${activePage == 'registration' ? 'active' : ''}" href="<c:url value='/registration' />">Регистрация</a>
            </sec:authorize>
        </nav>
    </div>
</header>
