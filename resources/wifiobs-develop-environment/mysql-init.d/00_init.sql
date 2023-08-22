CREATE
    USER 'wifiobs-local'@'localhost' IDENTIFIED BY 'wifiobs-local';
CREATE
    USER 'wifiobs-local'@'%' IDENTIFIED BY 'wifiobs-local';

GRANT ALL PRIVILEGES ON *.* TO
    'wifiobs-local'@'localhost';
GRANT ALL PRIVILEGES ON *.* TO
    'wifiobs-local'@'%';

CREATE
    DATABASE wifiobs DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE
DATABASE batch DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;