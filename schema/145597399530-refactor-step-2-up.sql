-- direction: up
-- backref: 145597306340
-- ref: 145597399530

use bank;

ALTER TABLE `bank`.`operation`
DROP COLUMN `month_adjusted`,
DROP COLUMN `month_bank`,
DROP COLUMN `montant`,
DROP INDEX `UNIQUE` ,
ADD UNIQUE INDEX `UNIQUE` (`compte` ASC, `date_operation` ASC, `date_valeur` ASC, `libelle` ASC, `debit` ASC, `credit` ASC),
DROP INDEX `YEAR_MONTH_CATEGO` ;


DROP VIEW `soldes`;
CREATE VIEW `soldes` AS
select
    `operation`.`compte` AS `compte`,
sum(credit) - sum(debit) solde
from
    `operation`
group by `operation`.`compte`;


USE `bank`;
CREATE
 OR REPLACE VIEW `bank`.`ops` AS
 select
        `bank`.`operation`.`year` AS `year`,
        `bank`.`operation`.`month` AS `month`,
        `bank`.`operation`.`catego` AS `catego`,
        sum(credit) AS `credit`,
        sum(debit) AS `debit`
    from
        `bank`.`operation`
    where
        (`bank`.`operation`.`compte` in ('CMB' , 'BPO'))
    group by `bank`.`operation`.`year` , `bank`.`operation`.`month` , `bank`.`operation`.`catego`;


    USE `bank`;
    CREATE  OR REPLACE VIEW `bank`.`diff` AS
        select
            `O`.`year` AS `year`,
            `O`.`month` AS `month`,
            `O`.`catego` AS `catego`,
            sum(O.credit) AS `ops_credit`,
            sum(O.debit) AS `ops_debit`,
            if((`B`.`credit` > 0), `B`.`credit`, 0) AS `bud_credit`,
            if((`B`.`debit` > 0), `B`.`debit`, 0) AS `bud_debit`,
            if(((`B`.`credit` > 0)
                    or (sum(O.credit) > 0)),
                1,
                0) AS `is_credit`
        from
            (`bank`.`operation` `O`
            left join `bank`.`budget` `B` ON (((`B`.`year` = `O`.`year`)
                and (`B`.`month` = `O`.`month`)
                and (`B`.`catego` = `O`.`catego`))))
        where
            ((`B`.`compte` = 'COURANT')
                or isnull(`B`.`compte`))
        group by `O`.`year` , `O`.`month` , `O`.`catego`
        union all select
            `B`.`year` AS `year`,
            `B`.`month` AS `month`,
            `B`.`catego` AS `catego`,
            0 AS `0`,
            0 AS `0`,
            `B`.`credit` AS `credit`,
            `B`.`debit` AS `debit`,
            if((`B`.`credit` > 0), 1, 0) AS `is_credit`
        from
            (`bank`.`budget` `B`
            left join `bank`.`operation` `O` ON (((`B`.`year` = `O`.`year`)
                and (`B`.`month` = `O`.`month`)
                and (`B`.`catego` = `O`.`catego`))))
        where
            isnull(`O`.`catego`)
        group by `B`.`year` , `B`.`month` , `B`.`catego`
        order by `year` , `month` , `is_credit` desc;
