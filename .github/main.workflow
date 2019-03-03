workflow "Build workflow" {
  on = "push"
  resolves = ["Build"]
}

workflow "Release workflow" {
  on = "release"
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
  args = ":site:orchidBuild -Penv=prod"
}
