-- direction: down
-- backref: 145263977200
-- ref: 145471933600

use bank;

DROP PROCEDURE `get_catego`;

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

