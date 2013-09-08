CREATE DATABASE IF NOT EXISTS yetifactory;
USE yetifactory;
CREATE TABLE IF NOT EXISTS posts (
  id          INT(11) NOT NULL AUTO_INCREMENT,
  title       VARCHAR(255) NOT NULL,
  body        TEXT,
  body_md     TEXT,
  snippet     TEXT,
  snippet_md  TEXT,
  created_at  DATETIME NOT NULL,
  updated_at  DATETIME NOT NULL,
  PRIMARY KEY(id)
);

