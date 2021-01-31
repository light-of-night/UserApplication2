DROP TABLE IF EXISTS t_user;
set character_set_results=utf8;
set character_set_client=utf8;
CREATE TABLE t_user  (
  id int primary key AUTO_INCREMENT,
  name varchar(32) unique ,
  password varchar(128) ,
  sex tinyint(1) ,
  photo varchar(255) ,
  birthDay date
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8;