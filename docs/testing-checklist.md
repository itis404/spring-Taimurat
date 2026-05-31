# Ручной чеклист проверки

## Infrastructure

- [ ] `docker compose up -d postgres redis`
- [ ] `/system/status` показывает PostgreSQL available
- [ ] `/system/status` показывает Redis available
- [ ] Flyway применил миграции V1-V6

## Auth

- [ ] `/registration` создаёт пользователя
- [ ] dev-ссылка подтверждает email
- [ ] login по username работает
- [ ] login по email работает
- [ ] неверный пароль возвращает `/login?error`
- [ ] `/logout` работает
- [ ] после logout `/profile` редиректит на `/login`

## Access control

- [ ] `user` не видит `/admin`
- [ ] `user` не видит `/articles/new`
- [ ] `writer` видит `/articles/new`
- [ ] `admin` видит `/admin` и `/admin/articles`

## Articles

- [ ] `/articles` показывает опубликованные статьи
- [ ] фильтр по тексту работает через AJAX
- [ ] фильтр по категории работает
- [ ] фильтр по тегу работает
- [ ] сортировка по названию работает
- [ ] writer создаёт статью
- [ ] admin публикует статью
- [ ] author редактирует свою статью

## Comments and reactions

- [ ] авторизованный пользователь оставляет комментарий
- [ ] пользователь удаляет свой комментарий
- [ ] admin удаляет любой комментарий
- [ ] лайк/дизлайк работают через AJAX

## REST API

- [ ] `GET /api/v1/articles` возвращает JSON
- [ ] `GET /api/v1/articles/{id}` возвращает JSON
- [ ] `POST /api/v1/articles` с Basic Auth writer создаёт статью
- [ ] невалидный POST возвращает JSON 400
- [ ] POST без auth возвращает 401
- [ ] Swagger UI открывается на `/swagger-ui.html`
- [ ] Postman collection импортируется

## External API and OAuth

- [ ] Email Reputation API вызывается при регистрации
- [ ] GitHub OAuth редиректит на GitHub
- [ ] callback создаёт локального пользователя
- [ ] `oauth_accounts` содержит запись `provider=GITHUB`
- [ ] повторный GitHub login не создаёт дубль

## Redis cache

- [ ] `/` открывается, в логах есть запись cache written/hit
- [ ] `/articles` открывается, статистика берётся из Redis после первого запроса
- [ ] `/admin` -> сбросить кэш работает
