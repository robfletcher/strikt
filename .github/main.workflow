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
  secrets = ["BINTRAY_USER", "BINTRAY_KEY"]
}

action "site" {
  uses = "MrRamych/gradle-actions@master"
  args = ":site:orchidDeploy -Penv=prod -Prelease.useLastTag=true"
  secrets = ["GITHUB_TOKEN"]
  needs = ["release"]
}

action "tweet" {
  uses = "xorilog/twitter-action@master"
  args = ["-message", "Strikt $GITHUB_REF `jq .name $GITHUB_EVENT_PATH --raw-output` is available. https://strikt.io\n\nRelease notes: https://github.com/$GITHUB_REPOSITORY/releases/$GITHUB_REF"]
  secrets = ["TWITTER_CONSUMER_KEY", "TWITTER_CONSUMER_SECRET", "TWITTER_ACCESS_TOKEN", "TWITTER_ACCESS_SECRET"]
  needs = ["message"]
}

action "message" {
  uses = "actions/bin/sh@master"
  args = ["echo \"Strikt $GITHUB_REF `jq .release.name $GITHUB_EVENT_PATH --raw-output` is available. https://strikt.io\n\nRelease notes: https://github.com/$GITHUB_REPOSITORY/releases/$GITHUB_REF\""]
  needs = ["release"]
}
