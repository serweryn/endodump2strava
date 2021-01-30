create table if not exists token_info (
    user varchar(255) not null,
    token_type varchar(255) not null,
    access_token varchar(255) not null,
    expires_at int not null,
    refresh_token varchar(255) not null,
    primary key (user)
) ;

create table if not exists imported_activity (
    workout_basename varchar(255) not null,
    upload_id bigint not null,
    activity_id bigint,
    primary key (workout_basename)
);

create table if not exists imported_activity_step (
    workout_basename varchar(255) not null,
    step_name varchar(255) not null,
    response_code int not null,
    response_body varchar(9999999) not null,
    response_headers varchar(9999999) not null,
    received timestamp not null,
    primary key (workout_basename, step_name)
);
