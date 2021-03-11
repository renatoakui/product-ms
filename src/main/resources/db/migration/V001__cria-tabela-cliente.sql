CREATE TABLE compasso.produto (
  id VARCHAR(30) NOT NULL,
  name VARCHAR(60) NOT NULL,
  description VARCHAR(200) NOT NULL,
  price DECIMAL(15,2) NOT NULL,
  PRIMARY KEY (id));