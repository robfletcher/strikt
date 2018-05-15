#!/bin/bash
#
# Copyright 2017 Pivotal Software, Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# This script will build the release.

SWITCHES="-s --console=plain"

if [ $CIRCLE_PR_NUMBER ]; then
  echo -e "Build Pull Request #$CIRCLE_PR_NUMBER => Branch [$CIRCLE_BRANCH]"
  ./gradlew clean build $SWITCHES
elif [ -z $CIRCLE_TAG ]; then
  echo -e ?'Build Branch with Snapshot => Branch ['$CIRCLE_BRANCH']'
  ./gradlew clean build $SWITCHES
elif [ $CIRCLE_TAG ]; then
  echo -e 'Build Branch for Release => Branch ['$CIRCLE_BRANCH']  Tag ['$CIRCLE_TAG']'
  case "$CIRCLE_TAG" in
  *-rc\.*)
    ./gradlew -Prelease.useLastTag=true clean build candidate $SWITCHES
    ;;
  *)
    ./gradlew -Prelease.useLastTag=true clean build final $SWITCHES
    ;;
  esac
else
  echo -e 'WARN: Should not be here => Branch ['$CIRCLE_BRANCH']  Tag ['$CIRCLE_TAG']  Pull Request ['$CIRCLE_PR_NUMBER']'
  ./gradlew clean build $SWITCHES
fi

EXIT=$?

exit $EXIT