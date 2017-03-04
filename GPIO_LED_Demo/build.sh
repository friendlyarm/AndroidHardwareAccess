#!/bin/bash


cd /opt/FriendlyARM/android_examples/jni-demos/GPIO_LED_Demo
rm -rf gen bin

cd /opt/FriendlyARM/smart4418/android/
rm -f GPIODemo-r1.0.apk
. setenv
mmm /opt/FriendlyARM/android_examples/jni-demos/GPIO_LED_Demo 
java -jar out/host/linux-x86/framework/signapk.jar vendor/friendly-arm/nanopi3/security/platform.x509.pem vendor/friendly-arm/nanopi3/security/platform.pk8 /opt/FriendlyARM/smart4418/android/out/target/product/nanopi3/obj/APPS/GPIODemo_intermediates/package.apk.unsigned GPIODemo-unaligned.apk
zipalign -v 4 GPIODemo-unaligned.apk GPIODemo-r1.0.apk
# out/host/linux-x86/bin/zipalign -f 4 out/target/product/nanopi3/obj/APPS/GPIODemo_intermediates/package.apk.unaligned out/target/product/nanopi3/obj/APPS/GPIODemo_intermediates/package.apk.aligned
adb install -r GPIODemo-r1.0.apk
