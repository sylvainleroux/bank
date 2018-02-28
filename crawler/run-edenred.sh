#!/bin/bash

# export PATH=$PATH:/usr/local/Cellar/node/4.2.1/lib/node_modules/phantomjs/lib/phantom/bin

PATH=$PATH:/usr/local/bin

rm -Rf tmp/*

phantomjs --cookies-file=tmp/cmb-cookies scripts/edenred.js

DATE_FORMATED=$(date +%Y-%M-%d-%H%M)
cp tmp/edenred.json ~/Downloads/Edenred-Export_$DATE_FORMATED.json

rm tmp/cmb-cookies
