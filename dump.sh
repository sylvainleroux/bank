DUMP_DATE=$(date +"%Y%m%d%H%m%s")

mysqldump -h192.168.1.17 -usleroux -p bank > ~/Dropbox/Comptes/dump_$DUMP_DATE.sql

ls -lh ~/Dropbox/Comptes/dump_$DUMP_DATE.sql
