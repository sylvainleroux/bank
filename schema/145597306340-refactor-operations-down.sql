-- direction: down
-- backref: 145487256030
-- ref: 145597306340

ALTER TABLE `bank`.`operation`
DROP COLUMN `debit`,
DROP COLUMN `credit`,
DROP COLUMN `month`,
DROP INDEX `YEAR_MONTH_CATEGO_N`;
