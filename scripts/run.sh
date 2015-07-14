#!/bin/sh -xe
WORKSPACE="$( cd "$( dirname "${BASH_SOURCE[0]}" )/../" && pwd )"


cd $WORKSPACE
./gradlew prepareBinaryForLocalDocker
#docker run -P -v $WORKSPACE/build/libs:/opt/ordering hippoom/restfriedchicken-jar