<<<<<<< HEAD
# FPVHub

FPVHub — MPA-форум и база знаний по FPV-квадрокоптерам. Проект реализован на Java 21, Spring Boot, Spring MVC, JSP/JSTL, Spring Security, PostgreSQL, Spring Data JPA, ручных JPA/Criteria-запросах, Redis, Docker Compose, REST API, OpenAPI, AJAX, внешнем Email Reputation API и ручном GitHub OAuth.

## Основные возможности

- публичная база статей по FPV-квадрокоптерам;
- регистрация, login/logout, подтверждение email;
- роли `USER`, `WRITER`, `ADMIN`;
- создание, редактирование и архивирование статей;
- модерация статей администратором;
- категории и теги;
- комментарии и реакции LIKE/DISLIKE;
- AJAX-поиск, фильтрация, реакции и удаление комментариев;
- REST API для `Article`;
- OpenAPI/Swagger UI;
- Postman collection;
- внешний Email Reputation API через HTTP без SDK;
- ручной GitHub OAuth без Spring OAuth Client;
- Redis-кэш публичных блоков;
- централизованная обработка MVC/REST/AJAX ошибок.

## Стек

- Java 21
- Maven
- Spring Boot 3.5.14
- Spring MVC
- JSP/JSTL
- Spring Security
- Spring Data JPA
- PostgreSQL 17
- Redis 7.4
- Flyway
- Docker Compose
- springdoc-openapi

## Демо-пользователи

| Логин | Пароль | Роли |
|---|---|---|
| `admin` | `admin12345` | USER, WRITER, ADMIN |
| `writer` | `writer12345` | USER, WRITER |
| `user` | `user12345` | USER |

## Запуск локально через IntelliJ IDEA

1. Поднять PostgreSQL и Redis:

```powershell
docker compose up -d postgres redis
```

2. Создать `.env` из `.env.example`.

3. Запустить `ru.itis.fpvhub.FpvHubApplication` из IntelliJ IDEA.

4. Открыть:

```text
http://localhost:8080/
```

## Полный запуск через Docker Compose

```powershell
docker compose up --build
```

Приложение будет доступно на:

```text
http://localhost:8080/
```

## Полезные URL

| URL | Назначение |
|---|---|
| `/` | Главная |
| `/articles` | Статьи, поиск и фильтрация |
| `/categories` | Категории |
| `/login` | Вход |
| `/registration` | Регистрация |
| `/profile` | Профиль |
| `/my/articles` | Статьи текущего автора |
| `/admin` | Админ-панель |
| `/admin/articles` | Модерация статей |
| `/system/status` | Проверка PostgreSQL/Redis |
| `/api/v1/articles` | REST API |
| `/v3/api-docs` | OpenAPI JSON |
| `/swagger-ui.html` | Swagger UI |

## REST API

Для сущности `Article` реализованы endpoints:

```text
GET    /api/v1/articles
GET    /api/v1/articles/{id}
POST   /api/v1/articles
PUT    /api/v1/articles/{id}
DELETE /api/v1/articles/{id}
```

`GET` доступен публично. `POST`, `PUT`, `DELETE` требуют HTTP Basic с ролью `WRITER` или `ADMIN`.

Postman collection:

```text
docs/api/FPVHub-stage6.postman_collection.json
```

## External Email Reputation API

Интеграция включается через переменные:

```text
EMAIL_VALIDATION_ENABLED=true
EMAIL_VALIDATION_FAIL_OPEN=false
EMAIL_VALIDATION_API_URL=https://emailreputation.abstractapi.com/v1/
EMAIL_VALIDATION_API_KEY=...
EMAIL_VALIDATION_PROXY_HOST=127.0.0.1
EMAIL_VALIDATION_PROXY_PORT=2080
```

Proxy-переменные нужны только если внешний API доступен через локальный proxy.

## GitHub OAuth

Интеграция включается через переменные:

```text
GITHUB_OAUTH_ENABLED=true
GITHUB_OAUTH_CLIENT_ID=...
GITHUB_OAUTH_CLIENT_SECRET=...
GITHUB_OAUTH_REDIRECT_URI=http://localhost:8080/oauth2/github/callback
```

GitHub OAuth App должен иметь callback URL:

```text
http://localhost:8080/oauth2/github/callback
```

## Redis cache

Кэшируются:

- последние статьи главной страницы;
- список категорий;
- блок статистики статей: популярные и обсуждаемые.

Администратор может сбросить публичный кэш из `/admin`.

## Документация

- `docs/project-description.md` — описание проекта;
- `docs/architecture.md` — архитектура и пакеты;
- `docs/security.md` — безопасность;
- `docs/er-diagram.md` — ER-диаграмма в Mermaid;
- `docs/testing-checklist.md` — чеклист ручной проверки;
- `docs/integration/stage7-external-api-and-oauth.md` — внешние интеграции.
=======
[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/Knv-_QWg)
>>>>>>> 2afbd6251b7fc50dbbe33b38821eb809bce52af9
