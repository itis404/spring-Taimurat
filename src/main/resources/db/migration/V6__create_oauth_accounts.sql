create table oauth_accounts
(
    id                bigserial primary key,
    user_id           bigint       not null references users (id) on delete cascade,
    provider          varchar(40)  not null,
    provider_user_id  varchar(120) not null,
    provider_username varchar(120) not null,
    provider_email    varchar(254),
    created_at        timestamptz  not null,
    constraint uk_oauth_accounts_provider_user unique (provider, provider_user_id)
);

create index idx_oauth_accounts_user_id on oauth_accounts (user_id);
