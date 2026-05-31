create table roles
(
    id           bigserial primary key,
    name         varchar(50)  not null unique,
    display_name varchar(100) not null
);

create table users
(
    id             bigserial primary key,
    username       varchar(40)                  not null unique,
    email          varchar(254)                 not null unique,
    password_hash  varchar(100)                 not null,
    enabled        boolean                      not null default false,
    email_verified boolean                      not null default false,
    created_at     timestamp with time zone     not null,
    updated_at     timestamp with time zone     not null
);

create table profiles
(
    id                   bigserial primary key,
    user_id              bigint                   not null unique references users (id) on delete cascade,
    display_name         varchar(80)              not null,
    bio                  varchar(1000),
    avatar_url           varchar(500),
    fpv_experience_level varchar(80),
    favorite_setup       varchar(500),
    created_at           timestamp with time zone not null,
    updated_at           timestamp with time zone not null
);

create table user_roles
(
    user_id bigint not null references users (id) on delete cascade,
    role_id bigint not null references roles (id) on delete cascade,
    primary key (user_id, role_id)
);

create table email_verification_tokens
(
    id         bigserial primary key,
    user_id    bigint                   not null references users (id) on delete cascade,
    token      varchar(120)             not null unique,
    expires_at timestamp with time zone not null,
    used_at    timestamp with time zone,
    created_at timestamp with time zone not null
);

create index idx_users_username_lower on users (lower(username));
create index idx_users_email_lower on users (lower(email));
create index idx_user_roles_user_id on user_roles (user_id);
create index idx_user_roles_role_id on user_roles (role_id);
create index idx_email_verification_tokens_token on email_verification_tokens (token);
