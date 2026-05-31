# FPVHub Stage 6 API

Stage 6 adds a REST API for the `Article` aggregate.

Endpoints:

- `GET /api/v1/articles` — public list of published articles.
- `GET /api/v1/articles/{id}` — public for published articles, private for drafts/moderation articles.
- `POST /api/v1/articles` — create article, requires `ROLE_WRITER` or `ROLE_ADMIN`.
- `PUT /api/v1/articles/{id}` — update article, requires author or admin.
- `DELETE /api/v1/articles/{id}` — archive article, requires author or admin.

OpenAPI:

- JSON: `/v3/api-docs`
- Swagger UI: `/swagger-ui.html`

For protected endpoints use HTTP Basic auth with one of the demo users:

- `writer / writer12345`
- `admin / admin12345`

CSRF is disabled only for `/api/**`; server-rendered MPA forms still use Spring Security CSRF protection.
