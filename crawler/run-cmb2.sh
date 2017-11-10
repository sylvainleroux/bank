#!/bin/bash

# export PATH=$PATH:/usr/local/Cellar/node/4.2.1/lib/node_modules/phantomjs/lib/phantom/bin

PATH=$PATH:/usr/local/bin

rm -Rf tmp/*

phantomjs --cookies-file=tmp/cmb-cookies scripts/cmb2.js

cat tmp/cmb-files | while read line; do
	eval $line;
done

rm tmp/cmb-cookies
