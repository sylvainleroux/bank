#!/usr/bin/env bash

# export PATH=$PATH:/usr/local/Cellar/node/4.2.1/lib/node_modules/phantomjs/lib/phantom/bin
# PATH=$PATH:/usr/local/bin

rm -Rf tmp/*
phantomjs --cookies-file=tmp/cmb-cookies scripts/cmb.js

cd ./tmp
cat cmb-files | while read line; do
	eval $line;
done

mv RELEVE_* ~/Downloads/
rm cmb-cookies
