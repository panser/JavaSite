CREATE DATABASE IF NOT EXISTS javasite
DEFAULT CHARACTER SET utf8;

USE javasite;
SET FOREIGN_KEY_CHECKS = 0;
#DROP TABLE IF EXISTS users;
#DROP TABLE IF EXISTS userAddress;
#DROP TABLE IF EXISTS articles;
#DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS visitors;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE IF NOT EXISTS users (
  id        BIGINT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  version   BIGINT(11) DEFAULT NULL,
  email     VARCHAR(50) DEFAULT NULL,
  login     VARCHAR(50) UNIQUE DEFAULT NULL,
  password  VARCHAR(50) DEFAULT NULL,
  enabled TINYINT DEFAULT 1,
  role VARCHAR(45) NOT NULL,
  sex TINYINT DEFAULT 1,
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

CREATE TABLE IF NOT EXISTS articles (
  id        BIGINT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  version   BIGINT(11) DEFAULT NULL,
  title     VARCHAR(250) DEFAULT NULL,
  description     VARCHAR(250) DEFAULT NULL,
  text     VARCHAR(20000) DEFAULT NULL,
  author_id   BIGINT(11) DEFAULT NULL,
  visible TINYINT DEFAULT 1,
  allowComments TINYINT DEFAULT 1,
  createDate   TIMESTAMP DEFAULT NOW(),
  deleteDate  DATETIME DEFAULT NULL
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8;

CREATE TABLE IF NOT EXISTS comments (
  id        BIGINT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  version   BIGINT(11) DEFAULT NULL,
  text     VARCHAR(250) DEFAULT NULL,
  author_id   BIGINT(11),
  email     VARCHAR(50) DEFAULT NULL,
  article_id   BIGINT(11),
  visible TINYINT DEFAULT 1,
#   comment_id   BIGINT(11),
  createDate   TIMESTAMP DEFAULT NOW(),
  deleteDate  DATETIME DEFAULT NULL
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8;

CREATE TABLE IF NOT EXISTS visitors (
  id        BIGINT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  version   BIGINT(11) DEFAULT NULL,
  ip     VARCHAR(250) DEFAULT NULL,
  userAgent     VARCHAR(250) DEFAULT NULL,
  article_id   BIGINT(11),
#   createDate   TIMESTAMP DEFAULT NOW(),
  createDate   DATETIME
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8;

# ADD FOREIGN KEYS
ALTER TABLE userAddress ADD CONSTRAINT fk_userAddress_user FOREIGN KEY (user_id) REFERENCES users(id)
  ON UPDATE CASCADE;
ALTER TABLE articles ADD CONSTRAINT fk_atricle_user FOREIGN KEY (author_id) REFERENCES users(id)
  ON UPDATE CASCADE;
ALTER TABLE comments ADD CONSTRAINT fk_comment_user FOREIGN KEY (author_id) REFERENCES users(id)
  ON UPDATE CASCADE;
ALTER TABLE comments ADD CONSTRAINT fk_comment_article FOREIGN KEY (article_id) REFERENCES articles(id)
  ON UPDATE CASCADE;
ALTER TABLE visitors ADD CONSTRAINT fk_visitor_article FOREIGN KEY (article_id) REFERENCES articles(id)
  ON UPDATE CASCADE;
