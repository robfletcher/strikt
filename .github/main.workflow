workflow "Build workflow" {
  on = "push"
  resolves = ["Build"]
}

action "Build" {
  uses = "MrRamych/gradle-actions@master"
  needs = ["Not gh-pages"]
  args = "build"
}

action "Not gh-pages" {
  uses = "actions/bin/filter@d820d56839906464fb7a57d1b4e1741cf5183efa"
  runs = "actions/bin/filter@master"
  args = "not branch gh-pages"
}
