create index idx_member_certification on member_tb (member_certification);

create index idx_member_id on wifi_service_tb (member_id);
# 오타로 인해 사용되지 않는 인덱스
create index idx_wifi_staus on wifi_service_tb (wifi_service_status);

create index idx_wifi_service_id_device_mac on device_tb (wifi_service_id, device_mac);

create index idx_wifi_service_id_device_id on connect_history_tb (wifi_service_id, device_id);
create index idx_start_time on connect_history_tb (start_time);

create index idx_wifi_service_id_device_id_day on connect_history_meta_tb (wifi_service_id, device_id, connect_history_meta_day);
create index idx_wifi_service_id_device_id_month on connect_history_meta_tb (wifi_service_id, device_id, connect_history_meta_month);

