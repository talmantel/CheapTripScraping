DROP TABLE IF EXISTS transportation_types;
CREATE TABLE transportation_types
(
    id   INT         NOT NULL,
    name VARCHAR(30) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
LOCK TABLES transportation_types WRITE;

INSERT INTO transportation_types (id, name) VALUES (1,'Flight'),
(2,'Bus'),
(3,'Train'),
(4,'Car Drive'),
(5,'Taxi'),
(6,'Walk'),
(7,'Town Car'),
(8,'Ride Share'),
(9,'Shuttle'),
(10,'Ferry'),
(11,'Subway');

UNLOCK TABLES;