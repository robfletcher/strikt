workflow "Build workflow" {
  on = "push"
  resolves = ["Build", "Echo secret"]
}

workflow "Release workflow" {
  on = "release"
  resolves = ["Deploy to GitHub Pages", "Tweet message"]
}

action "Echo secret" {
  uses = "actions/bin/debug@master"
  secrets = ["BINTRAY_USER"]
}

action "Filter branch" {
  uses = "actions/bin/filter@master"
  args = "not branch gh-pages"
}

action "Build" {
  uses = "MrRamych/gradle-actions@master"
  needs = ["Filter branch"]
  args = "build"
}

action "Release" {
  uses = "MrRamych/gradle-actions@master"
  args = "build final -Prelease.useLastTag=true"
  secrets = ["BINTRAY_USER", "BINTRAY_KEY"]
}

action "Build site" {
  uses = "MrRamych/gradle-actions@master"
  needs = ["Release"]
  args = ":site:orchidBuild -Penv=prod -Prelease.useLastTag=true"
}

action "Deploy to GitHub Pages" {
  uses = "maxheld83/ghpages@v0.2.1"
  needs = ["Build site"]
  env = {
    BUILD_DIR = "site/build/docs/orchid/"
  }
  secrets = ["GH_PAT"]
}

action "Tweet message" {
  uses = "xorilog/twitter-action@master"
  needs = ["Echo message"]
  args = ["-message", "Strikt $GITHUB_REF `jq .release.name $GITHUB_EVENT_PATH --raw-output` is available. https://strikt.io\n\nRelease notes: https://github.com/$GITHUB_REPOSITORY/releases/$GITHUB_REF"]
  secrets = ["TWITTER_CONSUMER_KEY", "TWITTER_CONSUMER_SECRET", "TWITTER_ACCESS_TOKEN", "TWITTER_ACCESS_SECRET"]
}

action "Echo message" {
  uses = "actions/bin/sh@master"
  needs = ["Release"]
  args = ["echo \"Strikt $GITHUB_REF `jq .release.name $GITHUB_EVENT_PATH --raw-output` is available. https://strikt.io\n\nRelease notes: https://github.com/$GITHUB_REPOSITORY/releases/$GITHUB_REF\""]
}
