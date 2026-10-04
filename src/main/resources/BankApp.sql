DROP DATABASE IF EXISTS bankapp_db;
CREATE DATABASE bankapp_db;
USE bankapp_db;

CREATE TABLE account (
  accountNo int NOT NULL,
  owner varchar(45) NOT NULL,
  balance double NOT NULL,
  PRIMARY KEY (accountNo)
);

INSERT INTO `account` VALUES (1,'Rip',100),(2,'Rap',200),(3,'Rup',300);