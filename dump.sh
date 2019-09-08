#!/bin/bash 

## Require mysqldump
## brew install mysql-client
## echo 'export PATH="/usr/local/opt/mysql-client/bin:$PATH"' >> ~/.bash_profile

DUMP_DATE=$(date +"%Y-%m-%d_%H%m%s")
HOST=192.168.1.71
DUMP_PATH=/Users/sleroux/Documents/Gestion/A48_Comptes

# touch ~/.my.cnf
# chmod 600 ~/.my.cnf
# [mysqldump]
# user=sleroux
# password=XXXXX

FILTER=tee

PATH_TO_PV=$(which pv)
if [ -x "${PATH_TO_PV}" ] ; then
  FILTER=${PATH_TO_PV}
else 
  echo ""
  echo "You could install pv : "
  echo "$ brew install pv"
  echo ""
fi 


echo "Start dump ..."
ssh iphone "mysqldump --routines bank" | $FILTER > ${DUMP_PATH}/dump_$DUMP_DATE.sql

ls -lh ${DUMP_PATH}/dump_$DUMP_DATE.sql

