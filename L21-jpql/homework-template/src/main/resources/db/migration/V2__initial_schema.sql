-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

create table address
(
    id serial not null primary key,
    client_id bigint,
    street varchar(50)
);

create table phone
(
    id serial not null primary key,
    client_id bigint,
    number varchar(50)
);
