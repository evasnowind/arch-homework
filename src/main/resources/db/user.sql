
CREATE DATABASE  IF NOT EXISTS  `user_db`  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;

CREATE TABLE user_db.user_info (
	id INTEGER auto_increment NOT null primary key,
	username varchar(32) NOT NULL,
	passwd varchar(256) NULL
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_general_ci;
