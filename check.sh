#!/usr/bin/env bash

set -xe

./gradlew :library:testDebugUnitTest
./gradlew :library:lintDebug
#./gradlew --no-daemon sonarqube -Dsonar.host.url=$SONAR_URL -Dsonar.login=$SONAR_TOKEN -Dsonar.analysis.mode=preview -Dsonar.gitlab.project_id=$CI_PROJECT_PATH -Dsonar.gitlab.commit_sha=$CI_COMMIT_SHA -Dsonar.gitlab.ref_name=$CI_COMMIT_REF_NAME
./gradlew --no-daemon sonarqube -Dsonar.host.url=$SONAR_URL -Dsonar.login=$SONAR_TOKEN