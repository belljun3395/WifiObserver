drop table if exists club_arrival_departure_list CASCADE;
drop table if exists member_mac_address_list CASCADE;

create table club_arrival_departure_list
(
    id         bigint NOT NULL AUTO_INCREMENT,
    local_date date,
    member_id  bigint,
    primary key (id)
);

create table member_mac_address_list
(
    id          bigint NOT NULL AUTO_INCREMENT,
    mac_address varchar(255),
    member_id   bigint,
    primary key (id)
);