create index if not exists idx_articles_title_lower on articles (lower(title));
create index if not exists idx_articles_summary_lower on articles (lower(summary));
create index if not exists idx_articles_views_count on articles (views_count desc);
create index if not exists idx_articles_video_url on articles (video_url) where video_url is not null;
