workflow "Build workflow" {
  on = "push"
  resolves = ["build"]
}

workflow "Release workflow" {
  on = "release"
  resolves = [
    "site",
    "tweet",
  ]
}

action "filter" {
  uses = "actions/bin/filter@master"
  args = "not branch gh-pages"
}

action "build" {
  uses = "MrRamych/gradle-actions@master"
  args = "build"
  needs = ["filter"]
}

action "release" {
  uses = "MrRamych/gradle-actions@master"
  args = "build final -Prelease.useLastTag=true"
  secrets = [
    "BINTRAY_USER",
    "BINTRAY_KEY"
  ]
}

action "site" {
  uses = "MrRamych/gradle-actions@master"
  args = ":site:orchidDeploy -Penv=prod -Prelease.useLastTag=true"
  secrets = ["GITHUB_TOKEN"]
  needs = ["release"]
}

action "message" {
  uses = "actions/bin/sh@master"
  args = [
    "apt-get update",
    "apt-get install --no-install-recommends -y jq",
    "RELEASE_TAG=`jq .release.tag_name $GITHUB_EVENT_PATH --raw-output` RELEASE_NAME=`jq .release.name $GITHUB_EVENT_PATH --raw-output` bash -c 'echo \"Strikt $RELEASE_TAG $RELEASE_NAME is available. https://strikt.io\n\nRelease notes: https://github.com/$GITHUB_REPOSITORY/releases/$RELEASE_TAG\" > tweet.txt'"
  ]
  needs = ["release"]
}

action "tweet" {
  uses = "./.github/actions/twitter-action"
  args = [
    "-file",
    "./tweet.txt"
  ]
  secrets = [
    "TWITTER_CONSUMER_KEY",
    "TWITTER_CONSUMER_SECRET",
    "TWITTER_ACCESS_TOKEN",
    "TWITTER_ACCESS_SECRET"
  ]
  needs = ["message"]
}
