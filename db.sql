use bank;

drop table if exists operation;

create table operation(
	id int not null auto_increment,
    compte varchar(255)  not null,
    date_operation date not null,
    date_valeur date not null,
    libelle  varchar(100) not null,
    montant  numeric(15,2) not null,
    catego varchar(50) default null,
    year int default null,
    month_bank int default null,
    month_adjusted int default null,
    primary key(id)
);

drop table if exists budget;

create table budget(
	id int unsigned not null auto_increment,
	year int unsigned not null,
	month int unsigned not null,
	catego varchar(50) not null,
	debit numeric(15,2) not null default 0,
	credit numeric(15,2) not null default 0,
	notes varchar(255) not null default "",
	primary key(id)
);


drop table if exists compte;

CREATE TABLE `compte` (
  `id` int(11) NOT NULL auto_increment,
  `nom` varchar(45) default NULL,
  `courant` tinyint(4) default NULL,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



truncate table bank.operation;
load  data local  infile "/Users/sleroux/Desktop/data.csv" 
INTO TABLE bank.operation 
fields terminated by '\t' enclosed by '\'' escaped by '\\'
(compte, date_operation, date_valeur, libelle, montant, catego, year, month, month_adjusted)
;



-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`sleroux`@`%` PROCEDURE `getMonthCredits`(IN in_year INT, IN in_month INT)
BEGIN
select T.catego, B.credit from
(select * from bank.budget where year = in_year and month = in_month and credit > 0 and compte = 'COURANT') B
right join 
(select distinct catego from bank.budget where credit > 0 and compte = 'COURANT') T
on B.catego = T.catego
order by catego
;


END

-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`sleroux`@`%` PROCEDURE `getMonthDebits`(IN in_year INT, IN in_month INT)
BEGIN
select T.catego, B.debit from
(select * from bank.budget where year = in_year and month = in_month and debit > 0 and compte = 'COURANT') B
right join 
(select distinct catego from bank.budget where debit > 0 and compte = 'COURANT') T
on B.catego = T.catego
order by catego
;


END