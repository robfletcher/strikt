workflow "Build workflow" {
  on = "push"
  resolves = ["Site"]
}

action "Filter gh-pages" {
  uses = "actions/bin/filter@master"
  args = "not branch gh-pages"
}

action "Build" {
  uses = "MrRamych/gradle-actions@master"
  needs = ["Filter gh-pages"]
  args = "build"
}

action "Release" {
  uses = "MrRamych/gradle-actions@master"
  args = "-Prelease.useLastTag=true final"
}

action "Site" {
  uses = "MrRamych/gradle-actions@master"
  needs = ["Build"]
  args = ":site:orchidBuild -Penv=prod"
}

