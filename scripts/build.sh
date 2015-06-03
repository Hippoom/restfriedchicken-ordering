#!/bin/sh -xe
WORKSPACE="$( cd "$( dirname "${BASH_SOURCE[0]}" )/../" && pwd )"



$WORKSPACE/gradlew clean

$WORKSPACE/gradlew build

python $WORKSPACE/scripts/deployment-config.py dev

$WORKSPACE/gradlew repackConfiguredArtifact -Penv=dev -Prepack=true

#python $WORKSPACE/scripts/ordering-service-deploy.py dev