#!/bin/bash

ANDROIDPATH=/opt/FriendlyARM/smart4418/android/
APPNAME=LCD1602
APKNAME=${APPNAME}-r1.0.apk
PRJPATH=$PWD
cd $PRJPATH
rm -rf bin gen

cd $ANDROIDPATH
rm -f $APKNAME
. setenv
mmm $PRJPATH -B
java -jar out/host/linux-x86/framework/signapk.jar vendor/friendly-arm/nanopi2/security/platform.x509.pem vendor/friendly-arm/nanopi2/security/platform.pk8 out/target/product/nanopi2/obj/APPS/${APPNAME}_intermediates/package.apk.unsigned ${APPNAME}-unaligned.apk
zipalign -v 4 ${APPNAME}-unaligned.apk $APKNAME
adb install -r ${APKNAME}
