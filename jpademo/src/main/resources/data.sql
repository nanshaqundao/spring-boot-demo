DROP TABLE IF EXISTS GenericEntities;

CREATE TABLE GenericEntities (
                              id INT AUTO_INCREMENT  PRIMARY KEY,
                              value VARCHAR(250) NOT NULL
);

INSERT INTO GenericEntities (value) VALUES
('luke');