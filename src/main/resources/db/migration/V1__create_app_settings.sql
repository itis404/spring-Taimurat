create table app_settings (
    id bigserial primary key,
    setting_key varchar(128) not null unique,
    setting_value text not null,
    created_at timestamp with time zone not null default now()
);

insert into app_settings (setting_key, setting_value)
values
    ('application.name', 'FPVHub'),
    ('application.stage', 'stage-1-skeleton');
