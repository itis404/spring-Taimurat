# Security documentation

## Аутентификация

Используется Spring Security form login:

- `/login` — вход;
- `/logout` — выход;
- пароли хранятся через BCrypt;
- вход возможен по username или email.

## Авторизация

Роли:

- `ROLE_USER` — базовый пользователь;
- `ROLE_WRITER` — создание и редактирование своих статей;
- `ROLE_ADMIN` — админка и модерация.

Закрытые зоны:

```text
/profile       authenticated
/my/articles   WRITER/ADMIN
/articles/new  WRITER/ADMIN
/admin/**      ADMIN
/api POST/PUT/DELETE WRITER/ADMIN
```

## CSRF

CSRF включён для MPA-форм. В JSP-формах передаётся `_csrf` token.

Для `/api/**` CSRF отключён, потому что REST API проверяется через HTTP Basic/Postman.

## XSS

Пользовательские данные выводятся через JSTL `<c:out>`, а не через прямой Java-код во view.

## SQL Injection

Нет ручной конкатенации SQL. Используются:

- Spring Data JPA;
- JPQL с параметрами;
- CriteriaBuilder.

## IDOR

Проверка владения ресурсом выполняется на service layer:

- пользователь может редактировать только свои статьи;
- администратор может модерировать все статьи;
- комментарий может удалить автор, автор статьи или администратор.

## Повторная отправка форм

Используется PRG-pattern:

```text
POST -> redirect -> GET
```

## External API policy

Email Reputation API вызывается при регистрации. Поведение при недоступности сервиса регулируется:

```text
EMAIL_VALIDATION_FAIL_OPEN=true/false
```

## OAuth state protection

GitHub OAuth flow использует `state`, который сохраняется в session и проверяется на callback.
