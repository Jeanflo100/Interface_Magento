CREATE TABLE product (
  idproduct INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  category VARCHAR(100) NOT NULL,
  family VARCHAR(100) NOT NULL,
  quality VARCHAR(100) NOT NULL,
  size VARCHAR(100) NOT NULL,
  actual_price DOUBLE NOT NULL,
  new_price DOUBLE NULL,
  active BOOLEAN NOT NULL,
  PRIMARY KEY (idproduct));
