create table if not exists statement_entity
(
    statement_id     varchar(255) not null primary key,
    last_modified_at timestamp    not null,
    value            double       not null,
    delivery_id      varchar(255) not null,
    courier_id       varchar(255) not null
);

create table if not exists delivery_created_entity
(
    id                varchar(255)   not null primary key,
    delivery_id       varchar(255)   not null,
    courier_id        varchar(255)   not null,
    created_timestamp timestamp      null,
    value             decimal(19, 2) null
);

create table if not exists bonus_modified_entity
(
    id                 varchar(255)   not null primary key,
    bonus_id           varchar(255)   not null,
    delivery_id        varchar(255)   not null,
    modified_timestamp timestamp      not null,
    value              decimal(19, 2) not null
);

create table if not exists adjustment_modified_entity
(
    id                 varchar(255)   not null primary key,
    adjustment_id      varchar(255)   not null,
    delivery_id        varchar(255)   not null,
    modified_timestamp timestamp      not null,
    value              decimal(19, 2) not null
);
