-- direction: up
-- backref: 146920807600
-- ref: 146984028150

use bank;

create table imported_balance (
  compte varchar(32) NOT NULL,
  solde decimal(15,2) NOT NULL default '0.00',
  last_update DATETIME
);
