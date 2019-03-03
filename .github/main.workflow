workflow "Build workflow" {
  on = "push"
  resolves = ["build"]
}

action "not gh-pages" {
  uses = "actions/bin/filter@master"
  args = "not branch gh-pages"
}

action "build" {
  uses = "MrRamych/gradle-actions@master"
  needs = ["not gh-pages"]
  args = "build"
}

