CREATE DATABASE IF NOT EXISTS blog;
USE blog;
CREATE TABLE IF NOT EXISTS posts (
  id          INT(11) NOT NULL AUTO_INCREMENT,
  title       VARCHAR(255) NOT NULL,
  slug        VARCHAR(255) NOT NULL,
  body        TEXT,
  body_md     TEXT,
  snippet     TEXT,
  created_at  DATETIME NOT NULL,
  updated_at  DATETIME NOT NULL,
  PRIMARY KEY(id)
);
ALTER TABLE posts ADD UNIQUE INDEX (slug);
