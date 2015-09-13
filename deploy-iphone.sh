#!/bin/bash



export IPHONE_IP=$(arp -a | grep fc:25:3f:65:67:86 | cut -d')' -f1 | cut -d'(' -f2)
export VERSION=`java -jar target/bank-jar-with-dependencies.jar columns:0 version`
export INSTALL_PATH=/private/var/lib/bank


# Header 
unzip -q -c target/bank-jar-with-dependencies.jar com/sleroux/bank/presentation/header.ascii
echo ""

echo "Create install directory"
ssh -A root@${IPHONE_IP} mkdir -p ${INSTALL_PATH}/${VERSION}
echo "Unlink previous versoin"
ssh -A root@${IPHONE_IP} rm -fv ${INSTALL_PATH}/current
echo "Copy Jar"
scp ./target/bank-jar-with-dependencies.jar root@${IPHONE_IP}:${INSTALL_PATH}/${VERSION}/
echo "Link new version"
ssh -A root@${IPHONE_IP} ln -sv ${INSTALL_PATH}/${VERSION} ${INSTALL_PATH}/current
echo "Copy launch script"
scp ./dist/iphone.sh  root@${IPHONE_IP}:${INSTALL_PATH}/${VERSION}/bank
ssh -A root@${IPHONE_IP} chmod +x ${INSTALL_PATH}/${VERSION}/bank




