workflow "Build workflow" {
  on = "push"
  resolves = ["Gradle Build"]
}

workflow "Release workflow" {
  on = "release"
  resolves = [
    "Publish Site",
    "Tweet Release Message",
  ]
}

action "Filter gh-pages branch" {
  uses = "actions/bin/filter@master"
  args = "not branch gh-pages"
}

action "Gradle Build" {
  uses = "MrRamych/gradle-actions/openjdk-11@2.0"
  args = "build"
  needs = ["Filter gh-pages branch"]
}

action "Release to Bintray" {
  uses = "MrRamych/gradle-actions/openjdk-11@2.0"
  args = "build final -Prelease.useLastTag=true"
  secrets = [
    "BINTRAY_USER",
    "BINTRAY_KEY",
  ]
}

action "Build Site" {
  uses = "MrRamych/gradle-actions/openjdk-11@2.0"
  args = ":site:orchidBuild -Penv=prod -Prelease.useLastTag=true"
  needs = ["Release to Bintray"]
}

action "Publish Site" {
  uses = "maxheld83/ghpages@v0.2.1"
  env = {
    BUILD_DIR = "site/build/docs/orchid/"
  }
  secrets = ["GH_PAT"]
  needs = ["Build Site"]
}

action "Create Release Message" {
  uses = "actions/bin/sh@master"
  args = [
    "apt-get update",
    "apt-get install --no-install-recommends -y jq",
    "RELEASE_TAG=`jq .release.tag_name $GITHUB_EVENT_PATH --raw-output` RELEASE_NAME=`jq .release.name $GITHUB_EVENT_PATH --raw-output` bash -c 'echo \"Strikt $RELEASE_TAG $RELEASE_NAME is available. https://strikt.io\n\nRelease notes: https://github.com/$GITHUB_REPOSITORY/releases/$RELEASE_TAG\" > tweet.txt'",
  ]
  needs = ["Release to Bintray"]
}

action "Tweet Release Message" {
  uses = "./.github/actions/twitter-action"
  args = [
    "-file",
    "./tweet.txt",
  ]
  secrets = [
    "TWITTER_CONSUMER_KEY",
    "TWITTER_CONSUMER_SECRET",
    "TWITTER_ACCESS_TOKEN",
    "TWITTER_ACCESS_SECRET",
  ]
  needs = ["Create Release Message"]
}
