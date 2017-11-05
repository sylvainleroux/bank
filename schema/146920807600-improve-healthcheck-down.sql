-- direction: down
-- backref: 145597399530
-- ref: 146920807600

USE bank;

DROP VIEW IF EXISTS analysis;

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
