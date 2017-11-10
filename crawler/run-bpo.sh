#!/bin/bash

PATH=$PATH:/usr/local/bin
rm -Rf tmp/*
# --remote-debugger-port=9000
phantomjs  --cookies-file=tmp/bpo-cookies scripts/bpo.js

mv tmp/BPO.json ~/Downloads/BPO.json
mv tmp/BPO_BALANCE.json ~/Downloads/BPO_BALANCE.json
rm tmp/bpo-cookies
