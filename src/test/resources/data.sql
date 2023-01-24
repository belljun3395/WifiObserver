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

insert
into member_mac_address_list
    (id, mac_address, member_id)
values (default, '90:32:4B:18:00:1B', 1);

insert
into member_mac_address_list
    (id, mac_address, member_id)
values (default, '38:68:93:6E:2B:20', 2);

insert
into member_mac_address_list
(id, mac_address, member_id)
values (default, '1A:E6:56:14:1B:F5', 3);

insert
into member_mac_address_list
(id, mac_address, member_id)
values (default, '38:68:93:6E:2B:20', 4);

-- member 1 : week = 2, month = 3, year = 4
insert
into club_arrival_departure_list
    (id, local_date, member_id)
values (default, now(), 1);

insert
into club_arrival_departure_list
(id, local_date, member_id)
values (default, DATE_SUB(NOW(), INTERVAL 3 DAY), 1);

insert
into club_arrival_departure_list
    (id, local_date, member_id)
values (default, DATE_SUB(NOW(), INTERVAL 8 DAY), 1);

insert
into club_arrival_departure_list
    (id, local_date, member_id)
values (default, DATE_SUB(NOW(), INTERVAL 40 DAY), 1);

-- member 2 : week = 1, month = 2, year = 3
insert
into club_arrival_departure_list
    (id, local_date, member_id)
values (default, now(), 2);

insert
into club_arrival_departure_list
    (id, local_date, member_id)
values (default, DATE_SUB(NOW(), INTERVAL 8 DAY), 2);

insert
into club_arrival_departure_list
(id, local_date, member_id)
values (default, DATE_SUB(NOW(), INTERVAL 40 DAY), 2);

-- member 3 : week = 1, month = 2, year = 2
insert
into club_arrival_departure_list
(id, local_date, member_id)
values (default, now(), 3);

insert
into club_arrival_departure_list
    (id, local_date, member_id)
values (default, DATE_SUB(NOW(), INTERVAL 8 DAY), 3);


-- member 4 : week = 1, month = 1, year = 1
insert
into club_arrival_departure_list
    (id, local_date, member_id)
values (default, now(), 4);