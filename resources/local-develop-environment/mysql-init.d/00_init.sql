CREATE
    USER 'observer-local'@'localhost' IDENTIFIED BY 'observer-local';
CREATE
    USER 'observer-local'@'%' IDENTIFIED BY 'observer-local';

GRANT ALL PRIVILEGES ON *.* TO
    'observer-local'@'localhost';
GRANT ALL PRIVILEGES ON *.* TO
    'observer-local'@'%';

CREATE
    DATABASE api DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE
    DATABASE batch DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;