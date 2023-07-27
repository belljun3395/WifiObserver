CREATE
    USER 'demo-local'@'localhost' IDENTIFIED BY 'demo-local';
CREATE
    USER 'demo-local'@'%' IDENTIFIED BY 'demo-local';

GRANT ALL PRIVILEGES ON *.* TO
    'demo-local'@'localhost';
GRANT ALL PRIVILEGES ON *.* TO
    'demo-local'@'%';

CREATE
    DATABASE demodb DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;