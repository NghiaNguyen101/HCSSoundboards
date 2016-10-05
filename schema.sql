DROP DATABASE IF EXISTS soundboard;
CREATE database soundboard;
use soundboard;

# User account tables
CREATE TABLE user
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  username VARCHAR(45) NOT NULL unique,
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

# Application logic tables
CREATE TABLE sound
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  sound MEDIUMBLOB NOT NULL,
  size INT(11) NOT NULL
);

CREATE TABLE board
(
  id int primary key AUTO_INCREMENT,
  name VARCHAR(100) not null,
  description varchar(1000) not null,
  ownerId int not null,
  public boolean not null,
  createDate datetime not null,
  FOREIGN KEY (ownerId) REFERENCES user (id)
);

CREATE TABLE board_x_sound
(
  boardId int,
  soundId int,
  soundName varchar(100) not null,
  public boolean not null,
  primary key (boardId, soundId),
  FOREIGN KEY (boardId) REFERENCES board (id),
  FOREIGN KEY (soundId) REFERENCES sound (id)
);