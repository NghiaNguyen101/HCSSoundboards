CREATE database soundboard;
use soundboard;

CREATE TABLE sound
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  sound MEDIUMBLOB NOT NULL,
  size INT(11) NOT NULL
);
CREATE TABLE user
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  username VARCHAR(45) NOT NULL,
  password VARCHAR(60) NOT NULL,
  enabled TINYINT(4) DEFAULT '1' NOT NULL
);
CREATE TABLE role
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  userId INT(11) NOT NULL,
  role VARCHAR(45) NOT NULL,
  CONSTRAINT fk_username FOREIGN KEY (userId) REFERENCES user (id)
);
CREATE INDEX fk_username ON role (userId);
CREATE UNIQUE INDEX uni_username_role ON role (role, userId);
