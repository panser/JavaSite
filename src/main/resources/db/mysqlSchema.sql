CREATE DATABASE IF NOT EXISTS javasite
DEFAULT CHARACTER SET utf8;

USE javasite;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS userAddress;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE IF NOT EXISTS users (
  id        BIGINT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  version   BIGINT(11) DEFAULT NULL,
  email     VARCHAR(50) DEFAULT NULL,
  login     VARCHAR(50) DEFAULT NULL,
  password  VARCHAR(50) DEFAULT NULL,
  enabled TINYINT NOT NULL DEFAULT 0,
  role VARCHAR(45) NOT NULL,
  sex TINYINT NOT NULL DEFAULT 0,
  avatarImage  LONGBLOB DEFAULT NULL,
  birthDay  DATETIME DEFAULT NULL,
  createDate   TIMESTAMP DEFAULT NOW(),
  deleteDate  DATETIME DEFAULT NULL
)
ENGINE =InnoDB
DEFAULT CHARSET =utf8;

CREATE TABLE IF NOT EXISTS userAddress (
  id        BIGINT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  version   BIGINT(11) DEFAULT NULL,
  country  VARCHAR(50) DEFAULT NULL,
  city  VARCHAR(50) DEFAULT NULL,
  street  VARCHAR(50) DEFAULT NULL,
  build   INT(11) DEFAULT NULL,
  user_id   BIGINT(11) NOT NULL
)
ENGINE =InnoDB
DEFAULT CHARSET =utf8;

# ADD FOREIGN KEYS
ALTER TABLE userAddress ADD CONSTRAINT fk_userAddress_user FOREIGN KEY (user_id) REFERENCES users(id)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
