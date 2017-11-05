-- direction: down
-- backref: 145597306340
-- ref: 145597399530

use bank;

ALTER TABLE `bank`.`operation`
  ADD COLUMN `montant` decimal(15,2) NULL AFTER `libelle`,
  ADD COLUMN `month_bank` INT(11) NULL AFTER `month`,
  ADD COLUMN `month_adjusted` INT(11) NULL AFTER `month_bank`,
  DROP INDEX `UNIQUE` ,
  ADD UNIQUE INDEX `UNIQUE` (`compte` ASC, `date_operation` ASC, `date_valeur` ASC, `libelle` ASC, `montant`),
  ADD INDEX `YEAR_MONTH_CATEGO` (`year` ASC, `month_adjusted` ASC, `catego` ASC);


DROP VIEW `soldes`;
CREATE VIEW `soldes` AS
      select
          `operation`.`compte` AS `compte`,
          sum(`operation`.`montant`) AS `solde`
      from
          `operation`
      group by `operation`.`compte`
;


CREATE OR REPLACE VIEW `ops` AS
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


USE `bank`;
CREATE  OR REPLACE VIEW `bank`.`diff` AS
    select
        `O`.`year` AS `year`,
        `O`.`month_adjusted` AS `month`,
        `O`.`catego` AS `catego`,
        sum((`O`.`montant` * (`O`.`montant` > 0))) AS `ops_credit`,
        sum((-(`O`.`montant`) * (`O`.`montant` < 0))) AS `ops_debit`,
        if((`B`.`credit` > 0), `B`.`credit`, 0) AS `bud_credit`,
        if((`B`.`debit` > 0), `B`.`debit`, 0) AS `bud_debit`,
        if(((`B`.`credit` > 0)
                or (sum((`O`.`montant` * (`O`.`montant` > 0))) > 0)),
            1,
            0) AS `is_credit`
    from
        (`bank`.`operation` `O`
        left join `bank`.`budget` `B` ON (((`B`.`year` = `O`.`year`)
            and (`B`.`month` = `O`.`month_adjusted`)
            and (`B`.`catego` = `O`.`catego`))))
    where
        ((`B`.`compte` = 'COURANT')
            or isnull(`B`.`compte`))
    group by `O`.`year` , `O`.`month_adjusted` , `O`.`catego`
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
            and (`B`.`month` = `O`.`month_adjusted`)
            and (`B`.`catego` = `O`.`catego`))))
    where
        isnull(`O`.`catego`)
    group by `B`.`year` , `B`.`month` , `B`.`catego`
    order by `year` , `month` , `is_credit` desc;
