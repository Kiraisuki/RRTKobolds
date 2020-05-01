#!/bin/sh

cd ..

./gradlew build

rm /home/kiraisuki/.local/share/multimc/instances/1.12.2/.minecraft/mods/rrtkobolds-0.1.jar

cp build/libs/rrtkobolds-0.1.jar /home/kiraisuki/.local/share/multimc/instances/1.12.2/.minecraft/mods/rrtkobolds-0.1.jar

multimc --launch "1.12.2"