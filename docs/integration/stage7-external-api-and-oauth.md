# Stage 7: external API and manual OAuth

## Email validation API

Registration uses `ExternalEmailValidationService`, which calls `EmailValidationApiClient`.
The client uses Spring `RestClient`, not an SDK.

Flow:

1. User submits registration form.
2. Application validates form locally.
3. Application checks username/email uniqueness in PostgreSQL.
4. Application sends HTTP request to AbstractAPI Email Reputation.
5. Invalid/disposable/undeliverable emails are rejected with form error.
6. If validation is successful, user is saved and email verification token is created.

## GitHub OAuth

The project does not use Spring OAuth Client.

Flow:

1. User clicks `Войти через GitHub`.
2. Application generates `state` and stores it in HTTP session.
3. User is redirected to GitHub authorization endpoint.
4. GitHub redirects back to `/oauth2/github/callback` with `code` and `state`.
5. Application validates `state`.
6. Application exchanges `code` for `access_token` through HTTP.
7. Application loads GitHub user profile and emails through HTTP.
8. Application finds or creates local `User` and `OAuthAccount`.
9. Application creates Spring Security authentication manually.

