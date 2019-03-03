workflow "Build workflow" {
  on = "push"
  resolves = ["Build"]
}

workflow "Release workflow" {
  on = "release"
  resolves = ["Deploy to GitHub Pages"]
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

action "Build site" {
  uses = "MrRamych/gradle-actions@master"
  args = ":site:orchidBuild -Penv=prod"
}

action "Deploy to GitHub Pages" {
  uses = "maxheld83/ghpages@v0.2.1"
  needs = ["Build site"]
  env = {
    BUILD_DIR = "site/build/docs/orchid/"
  }
  secrets = ["GH_PAT"]
}
