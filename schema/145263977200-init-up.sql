-- direction: up
-- ref: 145263977200

CREATE DATABASE  IF NOT EXISTS `bank` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `bank`;

-- MySQL dump 10.13  Distrib 5.6.27, for osx10.11 (x86_64)
--
-- Host: 192.168.1.71    Database: bank
-- ------------------------------------------------------
-- Server version	5.0.96

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Not dumping tablespaces as no INFORMATION_SCHEMA.FILES table on this server
--

--
-- Table structure for table `budget`
--

DROP TABLE IF EXISTS `budget`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `budget` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `year` int(10) unsigned NOT NULL,
  `month` int(10) unsigned NOT NULL,
  `catego` varchar(50) NOT NULL,
  `debit` decimal(15,2) NOT NULL default '0.00',
  `credit` decimal(15,2) NOT NULL default '0.00',
  `notes` varchar(255) NOT NULL default '',
  `compte` varchar(10) default NULL,
  PRIMARY KEY  (`id`),
  KEY `YEAR_MONTH` (`year`,`month`),
  KEY `COMPTE` (`compte`),
  KEY `YEAR_MONTH_CATEGO` (`year`,`month`,`catego`)
) ENGINE=InnoDB AUTO_INCREMENT=1910 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `budget_backup`
--

DROP TABLE IF EXISTS `budget_backup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `budget_backup` (
  `year` int(10) unsigned NOT NULL,
  `month` int(10) unsigned NOT NULL,
  `catego` varchar(50) NOT NULL,
  `debit` decimal(15,2) NOT NULL default '0.00',
  `credit` decimal(15,2) NOT NULL default '0.00',
  `notes` varchar(255) NOT NULL default '',
  `compte` varchar(10) default NULL,
  `timestamp` timestamp NOT NULL default CURRENT_TIMESTAMP,
  KEY `COMPARE` (`year`,`month`,`catego`,`compte`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPRESSED;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `budget_changes`
--

DROP TABLE IF EXISTS `budget_changes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `budget_changes` (
  `id` int(11) NOT NULL auto_increment,
  `year` int(11) NOT NULL,
  `month` tinyint(4) NOT NULL,
  `catego` varchar(100) NOT NULL,
  `compte` varchar(10) NOT NULL,
  `type` enum('CREDIT','DEBIT') NOT NULL,
  `prev_value` decimal(15,2) default NULL,
  `new_value` decimal(15,2) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=198 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `compte`
--

DROP TABLE IF EXISTS `compte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `compte` (
  `id` int(11) NOT NULL auto_increment,
  `nom` varchar(45) default NULL,
  `courant` tinyint(4) default NULL,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `operation`
--

DROP TABLE IF EXISTS `operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operation` (
  `id` int(11) NOT NULL auto_increment,
  `compte` varchar(255) NOT NULL,
  `date_operation` date NOT NULL,
  `date_valeur` date NOT NULL,
  `libelle` varchar(100) NOT NULL,
  `montant` decimal(15,2) NOT NULL,
  `catego` varchar(50) default NULL,
  `year` int(11) default NULL,
  `month_bank` int(11) default NULL,
  `month_adjusted` int(11) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `UNIQUE` (`compte`,`date_operation`,`date_valeur`,`libelle`,`montant`),
  KEY `COMPTE` (`compte`),
  KEY `YEAR_MONTH_CATEGO` (`year`,`month_adjusted`,`catego`)
) ENGINE=InnoDB AUTO_INCREMENT=3979 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `operation_backup`
--

DROP TABLE IF EXISTS `operation_backup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operation_backup` (
  `compte` varchar(255) NOT NULL,
  `date_operation` date NOT NULL,
  `date_valeur` date NOT NULL,
  `libelle` varchar(100) NOT NULL,
  `montant` decimal(15,2) NOT NULL,
  `catego` varchar(50) default NULL,
  `year` int(11) default NULL,
  `month_bank` int(11) default NULL,
  `month_adjusted` int(11) default NULL,
  `timestamp` timestamp NULL default CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;



USE `bank`;
CREATE  OR REPLACE VIEW `soldes` AS
 select 
        `bank`.`operation`.`compte` AS `compte`,
        sum(`bank`.`operation`.`montant`) AS `solde`
    from
        `bank`.`operation`
    group by `bank`.`operation`.`compte`;
;

CREATE VIEW `ops` AS
    select 
        `bank`.`operation`.`year` AS `year`,
        `bank`.`operation`.`month_adjusted` AS `month`,
        `bank`.`operation`.`catego` AS `catego`,
        sum((`bank`.`operation`.`montant` * (`bank`.`operation`.`montant` > 0))) AS `credit`,
        sum((-(`bank`.`operation`.`montant`) * (`bank`.`operation`.`montant` < 0))) AS `debit`
    from
        `operation`
    where
        (`bank`.`operation`.`compte` in (_latin1'CMB' , _latin1'BPO'))
    group by `bank`.`operation`.`year` , `bank`.`operation`.`month_adjusted` , `bank`.`operation`.`catego`
;

CREATE VIEW `diff` AS
    select 
        `o`.`year` AS `year`,
        `o`.`month_adjusted` AS `month`,
        `o`.`catego` AS `catego`,
        sum((`o`.`montant` * (`o`.`montant` > 0))) AS `ops_credit`,
        sum((-(`o`.`montant`) * (`o`.`montant` < 0))) AS `ops_debit`,
        if((`b`.`credit` > 0), `b`.`credit`, 0) AS `bud_credit`,
        if((`b`.`debit` > 0), `b`.`debit`, 0) AS `bud_debit`,
        if(((`b`.`credit` > 0)
                or (sum((`o`.`montant` * (`o`.`montant` > 0))) > 0)),
            1,
            0) AS `is_credit`
    from
        (`operation` `o`
        left join `budget` `b` ON (((`b`.`year` = `o`.`year`)
            and (`b`.`month` = `o`.`month_adjusted`)
            and (`b`.`catego` = `o`.`catego`))))
    where
        ((`b`.`compte` = _latin1'COURANT')
            or isnull(`b`.`compte`))
    group by `o`.`year` , `o`.`month_adjusted` , `o`.`catego` 
    union all select 
        `b`.`year` AS `year`,
        `b`.`month` AS `month`,
        `b`.`catego` AS `catego`,
        0 AS `0`,
        0 AS `0`,
        `b`.`credit` AS `credit`,
        `b`.`debit` AS `debit`,
        if((`b`.`credit` > 0), 1, 0) AS `is_credit`
    from
        (`budget` `b`
        left join `operation` `o` ON (((`b`.`year` = `o`.`year`)
            and (`b`.`month` = `o`.`month_adjusted`)
            and (`b`.`catego` = `o`.`catego`))))
    where
        isnull(`o`.`catego`)
    group by `b`.`year` , `b`.`month` , `b`.`catego`
    order by `year` , `month` , `is_credit` desc
;

CREATE VIEW `analysis` AS
    select 
        `ops`.`year` AS `year`,
        `ops`.`month` AS `month`,
        `ops`.`catego` AS `catego`,
        `ops`.`credit` AS `credit_ops`,
        `ops`.`debit` AS `debit_ops`,
        `b`.`credit` AS `credit_bud`,
        `b`.`debit` AS `debit_bud`,
        `b`.`notes` AS `notes`,
        (`ops`.`debit` > `b`.`debit`) AS `flag`
    from
        (`ops`
        left join `budget` `b` ON (((`ops`.`year` = `b`.`year`)
            and (`ops`.`month` = `b`.`month`)
            and (`ops`.`catego` = `b`.`catego`))))
    where
        (isnull(`b`.`catego`)
            or ((`ops`.`credit` - `b`.`credit`) <> 0)
            or ((`ops`.`debit` - `b`.`debit`) <> 0))
;

DELIMITER $$
CREATE PROCEDURE `get_catego`(IN libelle TEXT)
BEGIN

	declare i int default 1;
	declare s text default '';
	declare buffer  text default '';

	declare w_start int default 1;
	declare w_end int default 1;

	drop table if exists bank.categodef;
	create temporary table bank.categodef (
		entry varchar(100),
		catego varchar(100),
		nb int(11)
	);

	-- add space char at the end to simplify algo
	set libelle = concat(libelle," ");

	while i<= length(libelle) do
		
		IF substring(libelle,i,1) = " " THEN
			set w_end = i;
			set buffer = substring(libelle, w_start, w_end - w_start);
						
			IF buffer != "CARTE" AND (NOT buffer REGEXP "CB\:")
				AND buffer NOT REGEXP "[0-9]{6}"
				AND buffer != " "
				AND buffer NOT REGEXP "[0-9]{2}[[.slash.]][0-9]{2}"
			THEN
		
				insert into bank.categodef select buffer as entry, catego, count(*) nb from operation 
				where operation.libelle like concat("%",trim(buffer),"%") and catego is not null group by operation.catego;

				set s = concat(s, buffer, ",");
			END IF;

			
			set w_start = i;
		END IF;

		set i = i +1;
	end while;

	drop table if exists bank.categosum;
	create temporary table bank.categosum (
		entry varchar(100),
		sum int(11)
	);
	insert into bank.categosum select entry, sum(nb) sum from categodef group by entry;

	
	select 
		catego
	from 
	(
	select 
		categodef.entry,
		nb,
		catego,
		nb / categosum.sum ratio
	 from categodef inner join categosum on categodef.entry = categosum.entry
	) T
	group by catego
	order by std(ratio) desc;


END$$
DELIMITER ;







DELIMITER $$
CREATE PROCEDURE `getMonthCredits`(IN in_year INT, IN in_month INT)
BEGIN
select T.catego, B.credit from
(select * from bank.budget where year = in_year and month = in_month and credit > 0 and compte = 'COURANT') B
right join 
(select distinct catego from bank.budget where credit > 0 and compte = 'COURANT') T
on B.catego = T.catego
order by catego
;


END$$
DELIMITER ;



DELIMITER $$
CREATE PROCEDURE `getMonthDebits`(IN in_year INT, IN in_month INT)
BEGIN
select T.catego, B.debit from
(select * from bank.budget where year = in_year and month = in_month and debit > 0 and compte = 'COURANT') B
right join 
(select distinct catego from bank.budget where debit > 0 and compte = 'COURANT') T
on B.catego = T.catego
order by catego
;


END$$
DELIMITER ;





