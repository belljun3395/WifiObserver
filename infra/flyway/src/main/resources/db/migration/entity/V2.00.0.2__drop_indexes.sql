alter table member_tb drop index idx_member_certification;

alter table wifi_service_tb drop index idx_member_id;
alter table wifi_service_tb drop index idx_wifi_status;

alter table device_tb drop index idx_wifi_service_id_device_mac;

alter table connect_history_tb drop index idx_wifi_service_id_device_id;
alter table connect_history_tb drop index idx_start_time;

alter table connect_history_meta_tb drop index idx_wifi_service_id_device_id_day;
alter table connect_history_meta_tb drop index idx_wifi_service_id_device_id_month;

