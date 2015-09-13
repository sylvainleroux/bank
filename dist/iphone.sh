#!/bin/bash

export BANK_LIB=/private/var/lib/bank/current/bank-jar-with-dependencies.jar

if [ ! -f ${BANK_LIB} ]
then 
	echo "Library not found, project may have not been compiled yet"
	exit 1
fi
COLUMNS=$(tput cols)
java -cp ${BANK_LIB} com.sleroux.bank.Bank $@ columns:$COLUMNS


