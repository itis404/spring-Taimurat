create table categories (
    id bigserial primary key,
    name varchar(120) not null unique,
    slug varchar(140) not null unique,
    description varchar(500) not null,
    created_at timestamp with time zone not null
);

create table tags (
    id bigserial primary key,
    name varchar(80) not null unique,
    slug varchar(100) not null unique,
    created_at timestamp with time zone not null
);

create table articles (
    id bigserial primary key,
    title varchar(180) not null,
    slug varchar(220) not null unique,
    summary varchar(500) not null,
    content text not null,
    cover_image_url varchar(600),
    video_url varchar(600),
    status varchar(40) not null,
    views_count bigint not null default 0,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    published_at timestamp with time zone,
    author_id bigint not null references users(id),
    category_id bigint not null references categories(id)
);

create table article_tags (
    article_id bigint not null references articles(id) on delete cascade,
    tag_id bigint not null references tags(id) on delete cascade,
    primary key (article_id, tag_id)
);

create index idx_articles_status on articles(status);
create index idx_articles_author_id on articles(author_id);
create index idx_articles_category_id on articles(category_id);
create index idx_articles_published_at on articles(published_at);
create index idx_article_tags_tag_id on article_tags(tag_id);
