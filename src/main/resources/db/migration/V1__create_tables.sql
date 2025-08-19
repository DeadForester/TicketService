create table user_data
(
    user_data_id bigserial primary key,
    login        varchar(25) unique not null,
    password     varchar(25)        not null,
    fio          varchar(100)       not null
);

create table carrier
(
    carrier_id       serial primary key,
    name             varchar(100) unique not null,
    telephone_number varchar(12)
);

create table route
(
    route_id           serial primary key,
    point_of_departure varchar(50)      not null,
    destination        varchar(50)      not null,
    carrier_id         integer,
    travel_time        double precision not null,
    foreign key (carrier_id) references carrier (carrier_id)
);

create table ticket
(
    ticket_id       bigserial primary key,
    route_id        integer,
    user_data_id    bigint,
    local_date_time timestamp        not null,
    seat_number     integer          not null,
    price           double precision not null,
    foreign key (route_id) references route (route_id),
    foreign key (user_data_id) references user_data (user_data_id)
);