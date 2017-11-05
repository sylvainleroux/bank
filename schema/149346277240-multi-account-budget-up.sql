-- direction: up
-- backref: 148893024530
-- ref: 149346277240


ALTER TABLE `bank`.`budget`
CHANGE COLUMN `compte` `compte` VARCHAR(255) NOT NULL AFTER `month`,
CHANGE COLUMN `credit` `credit` DECIMAL(15,2) NOT NULL DEFAULT '0.00' AFTER `catego`,
CHANGE COLUMN `catego` `catego` VARCHAR(255) NOT NULL ;



ALTER TABLE `bank`.`compte`
DROP COLUMN `courant`,
ADD COLUMN `type` VARCHAR(45) NOT NULL DEFAULT 'CHECKING' AFTER `nom`;


ALTER TABLE `bank`.`budget`
DROP COLUMN `notes`,
DROP COLUMN `id`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`year`, `month`, `compte`, `catego`);


USE `bank`;
CREATE
 OR REPLACE VIEW `ops` AS
    SELECT
        `operation`.`year` AS `year`,
        `operation`.`month` AS `month`,
        `operation`.`compte` AS `compte`,
        `operation`.`catego` AS `catego`,
        SUM(`operation`.`credit`) AS `credit`,
        SUM(`operation`.`debit`) AS `debit`
    FROM
        `operation`
    WHERE 1
    GROUP BY `operation`.`year` , `operation`.`month` , `operation`.`compte`, `operation`.`catego`;




		USE `bank`;
		CREATE
		 OR REPLACE VIEW `analysis` AS
		SELECT
		        `ops`.`year` AS `year`,
		        `ops`.`month` AS `month`,
		         `ops`.`compte` AS `compte`,
		        `ops`.`catego` AS `catego`,
		        `ops`.`credit` AS `credit_ops`,
		        `ops`.`debit` AS `debit_ops`,
		        `b`.`credit` AS `credit_bud`,
		        `b`.`debit` AS `debit_bud`,
		        (`ops`.`debit` > `b`.`debit`) AS `flag`
		    FROM
		        (`ops`
		        LEFT JOIN `budget` `b` ON (((`ops`.`year` = `b`.`year`)
		            AND (`ops`.`month` = `b`.`month`)
		            AND (`ops`.`compte` = `b`.`compte`)
		            AND (`ops`.`catego` = `b`.`catego`))))
		    WHERE
		        (ISNULL(`b`.`catego`)
		            OR ((`ops`.`credit` - `b`.`credit`) <> 0)
		            OR ((`ops`.`debit` - `b`.`debit`) <> 0))
		    UNION ALL  SELECT
		        `b`.`year` AS `year`,
		        `b`.`month` AS `month`,
		        `b`.`compte` AS `compte`,
		        `b`.`catego` AS `catego`,
		        0 AS `credit_ops`,
		        0 AS `debit_ops`,
		        `b`.`credit` AS `credit_bud`,
		        `b`.`debit` AS `debit_bud`,
		        1 AS `flag`
		    FROM
		        (`budget` `b`
		        LEFT JOIN `ops` ON
					(`ops`.`year` = `b`.`year`)
					AND (`ops`.`month` = `b`.`month`)
		            AND  (`ops`.`compte` = `b`.`compte`)
					AND (`ops`.`catego` = `b`.`catego`))

		    WHERE
		        (ISNULL(`ops`.`year`)
		            AND (`b`.`month` <= MONTH(NOW()))
		            AND (`b`.`year` <= YEAR(NOW()))
		            AND ((`b`.`credit` > 0) OR (`b`.`debit` > 0)))
		    ORDER BY 1 , 2 , 3;;
