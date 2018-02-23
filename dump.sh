#!/bin/bash 

DUMP_DATE=$(date +"%Y-%m-%d_%H%m%s")
HOST=192.168.1.71

# touch ~/.my.cnf
# chmod 600 ~/.my.cnf
# [mysqldump]
# user=sleroux
# password=XXXXX

echo "Start dump ..."
mysqldump --routines  -h$HOST -usleroux bank > ~/Dropbox/Comptes/dump_$DUMP_DATE.sql

ls -lh ~/Dropbox/Comptes/dump_$DUMP_DATE.sql
