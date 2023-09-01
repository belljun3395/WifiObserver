CREATE TABLE member_tb
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    member_certification VARCHAR(255) NOT NULL,
    member_password      VARCHAR(255) NOT NULL,
    member_status        VARCHAR(40)  NOT NULL,
    create_at            DATETIME(6)  NOT NULL,
    update_at            DATETIME(6)  NOT NULL,
    deleted              BIT          NOT NULL,
    primary key (id)
);


CREATE TABLE wifi_service_tb (
    id BIGINT NOT NULL AUTO_INCREMENT,
    wifi_service_type VARCHAR(100) NOT NULL,
    wifi_service_cycle INT NOT NULL,
    wifi_service_standard_time INT NOT NULL,
    wifi_service_status VARCHAR(32) NOT NULL,
    member_id BIGINT NOT NULL,
    wifi_auth_id BIGINT NOT NULL,
    create_at DATETIME(6) NOT NULL,
    update_at DATETIME(6) NOT NULL,
    deleted BIT NOT NULL,
    primary key (id)
);

CREATE TABLE wifi_auth_tb (
    id BIGINT NOT NULL AUTO_INCREMENT,
    wifi_auth_host VARCHAR(100) NOT NULL,
    wifi_auth_certification VARCHAR(100) NOT NULL,
    wifi_auth_password VARCHAR(100) NOT NULL,
    create_at DATETIME(6) NOT NULL,
    update_at DATETIME(6) NOT NULL,
    deleted BIT NOT NULL,
    primary key (id)
);

CREATE TABLE device_tb (
    id BIGINT NOT NULL AUTO_INCREMENT,
    device_type VARCHAR(100) NOT NULL,
    device_mac VARCHAR(100) NOT NULL,
    wifi_service_id BIGINT NOT NULL,
    create_at DATETIME(6) NOT NULL,
    update_at DATETIME(6) NOT NULL,
    deleted BIT NOT NULL,
    primary key (id)
);

CREATE TABLE connect_history_tb (
    id BIGINT NOT NULL AUTO_INCREMENT,
    wifi_service_id BIGINT NOT NULL,
    device_id BIGINT NOT NULL,
    start_time DATETIME(6) NOT NULL,
    create_at DATETIME(6) NOT NULL,
    update_at DATETIME(6) NOT NULL,
    deleted BIT NOT NULL,
    primary key (id)
);

CREATE TABLE dis_connect_history_tb (
    id BIGINT NOT NULL AUTO_INCREMENT,
    wifi_service_id BIGINT NOT NULL,
    device_id BIGINT NOT NULL,
    end_time DATETIME(6) NOT NULL,
    create_at DATETIME(6) NOT NULL,
    update_at DATETIME(6) NOT NULL,
    deleted BIT NOT NULL,
    primary key (id)
);

CREATE TABLE connect_history_meta_tb (
    id BIGINT NOT NULL AUTO_INCREMENT,
    connect_history_meta_time_day TIME NOT NULL,
    connect_history_meta_time_month TIME NOT NULL,
    connect_history_meta_month DATE NOT NULL,
    connect_history_meta_day DATE NOT NULL,
    wifi_service_id BIGINT NOT NULL,
    device_id BIGINT NOT NULL,
    create_at DATETIME(6) NOT NULL,
    update_at DATETIME(6) NOT NULL,
    deleted BIT NOT NULL,
    primary key (id)
);
