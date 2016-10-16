DROP DATABASE IF EXISTS soundboard;
CREATE DATABASE soundboard;
USE soundboard;

# User account tables
CREATE TABLE user
(
  id       INT(11) PRIMARY KEY    NOT NULL AUTO_INCREMENT,
  username VARCHAR(45)            NOT NULL UNIQUE,
  password VARCHAR(60)            NOT NULL,
  enabled  TINYINT(4) DEFAULT '1' NOT NULL
);
CREATE TABLE role
(
  id     INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  userId INT(11)             NOT NULL,
  role   VARCHAR(45)         NOT NULL,
  CONSTRAINT fk_username FOREIGN KEY (userId) REFERENCES user (id)
);
CREATE INDEX fk_username
  ON role (userId);
CREATE UNIQUE INDEX uni_username_role
  ON role (role, userId);

# Application logic tables
CREATE TABLE sound
(
  id    INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  sound MEDIUMBLOB          NOT NULL,
  size  INT(11)             NOT NULL
);

CREATE TABLE board
(
  id         INT PRIMARY KEY AUTO_INCREMENT,
  ownerId    INT       NOT NULL,
  hidden     BOOLEAN   NOT NULL,
  createDate TIMESTAMP NOT NULL,
  FOREIGN KEY (ownerId) REFERENCES user (id)
);

CREATE TABLE board_version
(
  boardId     INT           NOT NULL,
  shared      BOOLEAN       NOT NULL,
  title       VARCHAR(100)  NOT NULL,
  description VARCHAR(1000) NOT NULL,
  updateDate  TIMESTAMP     NOT NULL
);

CREATE TABLE board_x_sound
(
  boardId   INT,
  shared    BOOLEAN      NOT NULL,
  soundId   INT,
  soundName VARCHAR(100) NOT NULL,
  PRIMARY KEY (boardId, soundId, shared),
  FOREIGN KEY (boardId) REFERENCES board (id),
  FOREIGN KEY (soundId) REFERENCES sound (id)
);