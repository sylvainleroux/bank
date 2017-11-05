-- direction: up
-- backref: 145471933600
-- ref: 145487256030

use bank;


CREATE TABLE `extract` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `extract_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

