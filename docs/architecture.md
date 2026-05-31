# Архитектура FPVHub

Проект построен по MVC и layered architecture.

```text
Controller -> Service -> Repository/DAO -> PostgreSQL
```

Дополнительные направления:

```text
JSP Views <- Controller
REST Controller -> Service -> DTO
AJAX Controller -> Service -> JSON DTO
External API Client -> AbstractAPI Email Reputation
OAuth Controller -> GitHub OAuth Client -> GitHub REST API
Cache Service -> Redis
```

## Пакеты

| Пакет | Ответственность |
|---|---|
| `controller` | MPA-контроллеры, JSP-страницы, AJAX endpoints |
| `rest` | REST API для Article |
| `service` | бизнес-логика, транзакции, проверки доступа |
| `repository` | Spring Data JPA repositories |
| `dao` | ручные JPA/CriteriaBuilder-запросы |
| `entity` | JPA-сущности |
| `dto` | DTO/view models/API responses |
| `form` | form/request-классы для JSP |
| `converter` | ручные конвертеры Entity <-> DTO/Form |
| `security` | Spring Security, UserDetailsService, Principal |
| `client` | HTTP-клиенты внешних API |
| `cache` | Redis-кэш публичных read-models |
| `exception` | доменные исключения |
| `util` | утилиты, например генерация slug |

## Главный CRUD

Полный CRUD реализован для сущности `Article`:

- создание статьи автором;
- просмотр статьи;
- редактирование своей статьи;
- архивирование своей статьи;
- модерация администратором;
- REST API для внешнего доступа.

## JPA и запросы

Используются:

- Spring Data JPA repositories;
- JPQL через `@Query`;
- CriteriaBuilder в `ArticleSearchDao`;
- subselect в `ArticleRepository.findDiscussedPublishedArticles`.

## Redis-кэш

Кэш находится в пакете `cache`.

Ключи:

```text
fpvhub:cache:latest-articles
fpvhub:cache:category-options
fpvhub:cache:article-statistics
```

Кэш инвалидируется при изменении статей и комментариев.
