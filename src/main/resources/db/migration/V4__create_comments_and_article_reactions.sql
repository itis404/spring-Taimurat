create table comments (
    id bigserial primary key,
    content varchar(2000) not null,
    deleted boolean not null default false,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    article_id bigint not null references articles(id) on delete cascade,
    author_id bigint not null references users(id) on delete cascade
);

create table article_reactions (
    id bigserial primary key,
    article_id bigint not null references articles(id) on delete cascade,
    user_id bigint not null references users(id) on delete cascade,
    type varchar(20) not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    constraint uk_article_reactions_article_user unique (article_id, user_id),
    constraint chk_article_reactions_type check (type in ('LIKE', 'DISLIKE'))
);

create index idx_comments_article_id on comments(article_id);
create index idx_comments_author_id on comments(author_id);
create index idx_comments_created_at on comments(created_at);
create index idx_article_reactions_article_id on article_reactions(article_id);
create index idx_article_reactions_user_id on article_reactions(user_id);
create index idx_article_reactions_type on article_reactions(type);
