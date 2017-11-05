-- direction: up
-- backref: 145597399530
-- ref: 146920807600

USE bank;

DROP VIEW IF EXISTS analysis;

CREATE VIEW `analysis` AS select `ops`.`year` AS `year`,`ops`.`month` AS `month`,`ops`.`catego` AS `catego`,`ops`.`credit` AS `credit_ops`,`ops`.`debit` AS `debit_ops`,`b`.`credit` AS `credit_bud`,`b`.`debit` AS `debit_bud`,`b`.`notes` AS `notes`,(`ops`.`debit` > `b`.`debit`) AS `flag` from (`ops` left join `budget` `b` on(((`ops`.`year` = `b`.`year`) and (`ops`.`month` = `b`.`month`) and (`ops`.`catego` = `b`.`catego`)))) where (isnull(`b`.`catego`) or ((`ops`.`credit` - `b`.`credit`) <> 0) or ((`ops`.`debit` - `b`.`debit`) <> 0)) union all select `b`.`year` AS `year`,`b`.`month` AS `month`,`b`.`catego` AS `catego`,0 AS `credit_ops`,0 AS `debit_ops`,`b`.`credit` AS `credit_bud`,`b`.`debit` AS `debit_bud`,`b`.`notes` AS `notes`,1 AS `flag` from (`budget` `b` left join `ops` on(((`ops`.`catego` = `b`.`catego`) and (`ops`.`month` = `b`.`month`) and (`ops`.`year` = `b`.`year`)))) where (isnull(`ops`.`year`) and (`b`.`compte` = _latin1'COURANT') and (`b`.`month` <= month(now())) and (`b`.`year` <= year(now())) and ((`b`.`credit` > 0) or (`b`.`debit` > 0))) order by 1,2,3;
