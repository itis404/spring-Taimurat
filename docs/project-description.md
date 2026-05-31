# FPVHub — описание проекта

FPVHub — MPA-форум и база знаний по FPV-квадрокоптерам. Пользователи читают статьи, регистрируются, подтверждают email, оставляют комментарии и реакции. Пользователи с ролью `WRITER` создают статьи и отправляют их на модерацию. Администратор публикует, отклоняет и архивирует статьи.

## Цель

Сайт решает задачу систематизации знаний по FPV: сборка, Betaflight, ELRS, видеосвязь, аккумуляторы, безопасность и пилотирование.

## Пользовательские роли

- `GUEST` — читает публичные статьи и категории;
- `USER` — пишет комментарии и ставит реакции;
- `WRITER` — создаёт и редактирует свои статьи;
- `ADMIN` — модерирует статьи, видит админ-панель, сбрасывает Redis-кэш.

## Основные сущности

- `User`
- `Role`
- `Profile`
- `Article`
- `Category`
- `Tag`
- `Comment`
- `ArticleReaction`
- `EmailVerificationToken`
- `OAuthAccount`

## Связи

- `User` M2M `Role`
- `Article` M2M `Tag`
- `User` O2M `Article`
- `Category` O2M `Article`
- `Article` O2M `Comment`
- `Article` O2M `ArticleReaction`
- `User` O2M `OAuthAccount`

## Используемые технологии

- Java 21
- Spring Boot
- Spring MVC
- JSP/JSTL
- Spring Security
- PostgreSQL
- Spring Data JPA
- CriteriaBuilder
- Flyway
- Redis
- Docker Compose
- REST API
- OpenAPI/Swagger UI
- Postman
- AJAX
- AbstractAPI Email Reputation
- ручной GitHub OAuth

## Выполнение требований

| Требование | Реализация |
|---|---|
| MPA | JSP-страницы через Spring MVC |
| HTML5/CSS3 | чистая вёрстка в `static/css/main.css` |
| Шаблоны | JSP fragments: header/footer |
| Регистрация/auth | Spring Security + BCrypt |
| Авторизация | role-based access |
| PostgreSQL | основная БД |
| Spring Data JPA | repositories |
| JPA/CriteriaBuilder | `ArticleSearchDao` |
| 6+ сущностей | реализовано 10 сущностей |
| M2M/O2M | User-Role, Article-Tag, Article-Comment |
| CRUD | Article CRUD |
| AJAX | поиск, реакции, удаление комментариев |
| External API | Email Reputation API без SDK |
| REST API | `/api/v1/articles` |
| OpenAPI | `/v3/api-docs`, `/swagger-ui.html` |
| Конвертеры | ручные converters |
| Логирование | SLF4J/Logback |
| @Query | ArticleRepository |
| Subselect | discussed articles query |
| Redis | публичные read-model кэши |
| Docker | app, postgres, redis |
| OAuth доп. | ручной GitHub OAuth |
