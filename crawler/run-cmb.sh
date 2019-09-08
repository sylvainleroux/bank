#!/usr/bin/env bash

 export PATH=$PATH:/Users/sleroux/.nvm/versions/node/v8.11.1/bin
 PATH=$PATH:/usr/local/bin

rm -Rf tmp/*
phantomjs --cookies-file=tmp/cmb-cookies scripts/cmb.js

cd ./tmp
cat cmb-files | while read line; do
	eval $line;
done

mv RELEVE_* ~/Downloads/
rm cmb-cookies
