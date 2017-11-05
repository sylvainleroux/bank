-- direction: down
-- backref: 148892899340
-- ref: 148893024530

USE `bank`;

CREATE
     OR REPLACE
VIEW `bank`.`ops` AS
    SELECT
        `bank`.`operation`.`year` AS `year`,
        `bank`.`operation`.`month` AS `month`,
        `bank`.`operation`.`catego` AS `catego`,
        SUM(`bank`.`operation`.`credit`) AS `credit`,
        SUM(`bank`.`operation`.`debit`) AS `debit`
    FROM
        `bank`.`operation`
    WHERE
        (`bank`.`operation`.`compte` IN (_LATIN1'CMB' , _LATIN1'BPO'))
    GROUP BY `bank`.`operation`.`year` , `bank`.`operation`.`month` , `bank`.`operation`.`catego`;
