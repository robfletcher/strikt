#!/usr/bin/env sh
function require {
  command -v $1 >/dev/null 2>&1 || {
    echo >&2 "Script requires $1 but it's not available."
    exit 1
  }
}
require jq
require pup

if [ -z "$GITHUB_TOKEN" ]
then
  echo >&2 "Script requires a GITHUB_TOKEN environment variable but none exists."
  exit 1
fi

PREVIOUS=`curl 'https://api.github.com/repos/robfletcher/strikt/releases?per_page=1000' -H "Authorization: token $GITHUB_TOKEN" -s | jq '.[] | .name' | tr -d "\"" | sort`
NAMES=`curl 'http://www.voidstate.com/name_generator/index.php?action=display_results' -s --data 'action=display_results&dd1%5B%5D=All_Adjective&dd1%5B%5D=All_Noun&numberToDisplay=10' | pup 'table td strong text{}' | sort`
while read name
do
  if grep -q "$name" <<< "$PREVIOUS"
  then
    echo "~~ $name ~~ (ALREADY USED)"
  else
    echo "$name"
  fi
done <<< "$NAMES"
