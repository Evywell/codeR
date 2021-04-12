-- Creating databases
CREATE DATABASE IF NOT EXISTS `config`;
CREATE DATABASE IF NOT EXISTS `players`;
CREATE DATABASE IF NOT EXISTS `world`;

-- Grant all privileges on all databases for testing user
GRANT ALL PRIVILEGES ON *.* TO 'testing'@'%';
